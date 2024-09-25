package com.lepl.api.task;

import com.lepl.Service.task.ListsService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import com.lepl.util.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/lists")
public class ListsApiController {

  private final ListsService listsService;

  /**
   * 일정 조회 1과 2는 프론트에서 사용X -> 그냥 서버에서 확인용으로 만들어둔 것
   * 프론트는 일정 조회 3, 4 사용 -> findAllWithMemberTask, findByDateWithMemberTask
   */

  /**
   * 일정 조회(1) - 모든 Lists(=하루단위 일정모음) 조회
   */
  @GetMapping("/all")
  public List<ListsResDto> findAllWithTask() {
    List<Lists> lists = listsService.findAllWithTask();
    List<ListsResDto> result = lists.stream()
        .map(o -> new ListsResDto(o))
        .collect(Collectors.toList());
    return result;
  }

  /**
   * 일정 조회(2) - 특정 Lists(=하루단위 일정모음) 조회 - listsId 를 통해서
   */
  @GetMapping(value = "/{listsId}")
  public List<TaskDto> findOneWithTask(@PathVariable("listsId") Long listsId) {
    List<Lists> lists = listsService.findOneWithTask(listsId);
    if (lists.isEmpty()) {
      return null;
    }
    List<TaskDto> result = lists.get(0).getTasks().stream()
        .map(o -> new TaskDto(o))
        .collect(Collectors.toList());
    return result;
  }

  /**
   * 일정 조회(3) - 모든 Lists(=하루단위 일정모음) 조회 -> 해당 회원꺼만
   */
  @GetMapping(value = "/member/all")
  public ResponseEntity<List<ListsResDto>> findAllWithMemberTask(@Login Long memberId) {
    List<Lists> listsList = listsService.findAllWithMemberTask(memberId);
    if (listsList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    List<ListsResDto> result = listsList.stream()
        .map(o -> new ListsResDto(o))
        .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  /**
   * 일정 조회(4) - 날짜범위로 Lists(=하루단위 일정모음) 조회 -> 해당 회원꺼만 하루, 한달, 1년 등등 원하는 날짜 범위만큼 사용 가능
   */
  @PostMapping(value = "/member/date")
  public ResponseEntity<ApiResponse<List<ListsResDto>>> findByDateWithMemberTask(@Login Long memberId,
      @RequestBody @Validated CreateListsRequestDto request, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.info("검증 오류 발생 errors={}", bindingResult);
      ApiResponse res = ApiResponse.error(HttpStatus.BAD_REQUEST.value(), bindingResult);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
    List<Lists> listsList = listsService.findByDateWithMemberTask(memberId, request.startTime,
        request.endTime);
    if (listsList.isEmpty()) {
      ApiResponse res = ApiResponse.success(HttpStatus.NO_CONTENT.value(), null);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res);
    }
    List<ListsResDto> result = listsList.stream()
        .map(o -> new ListsResDto(o))
        .collect(Collectors.toList());
    ApiResponse res = ApiResponse.success(HttpStatus.OK.value(), result);
    return ResponseEntity.status(HttpStatus.OK).body(res);
  }

  /**
   * 일정 삭제 => memberId 까지 확인해서 안정성을 높이겠음. + cascade 사용
   */
  @PostMapping(value = "/member/delete")
  public ResponseEntity<String> delete(@Login Long memberId,
      @RequestBody DeleteRequestDto request) {
    Lists lists = listsService.findOneWithMemberTask(memberId, request.getListsId());
    if (lists == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이미 삭제된 일정입니다."); // 404
    }
    listsService.remove(lists);
    return ResponseEntity.status(HttpStatus.OK).body("해당 일정이 삭제되었습니다."); // 200
  }


  // DTO
  @Getter
  static class ListsResDto {

    private Long listsId;
    private LocalDate listsDate; // 등록 날짜
    private String timerAllUseTime; // 타이머총사용시간
    private List<TaskDto> listsTasks;

    public ListsResDto(Lists lists) { // lazy 강제 초기화
      listsId = lists.getId();
      listsDate = lists.getListsDate();
      Long time = lists.getTimerAllUseTime();
      Long hour = time / (60 * 60 * 1000);
      time %= (60 * 60 * 1000);
      Long minute = time / (60 * 1000);
      time %= (60 * 1000);
      Long second = time / (1000);
      timerAllUseTime = hour + ":" + minute + ":" + second; // 시:분:초 형태로 반환
      listsTasks = lists.getTasks().stream()
          .map(o -> new TaskDto(o))
          .collect(Collectors.toList());
    }
  }

  @Getter
  static class TaskDto {

    private Long taskId;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String remainTime;
    private Boolean completedStatus;
    private Boolean timerOnOff;

    public TaskDto(Task task) { // lazy 강제 초기화
      taskId = task.getId();
      content = task.getContent();
      startTime = task.getStartTime();
      endTime = task.getEndTime();
      Long time = task.getRemainTime();
      Long hour = time / (60 * 60 * 1000);
      time %= (60 * 60 * 1000);
      Long minute = time / (60 * 1000);
      time %= (60 * 1000);
      Long second = time / (1000);
      remainTime = hour + ":" + minute + ":" + second; // 시:분:초 형태로 반환
      completedStatus = task.getTaskStatus().getCompletedStatus();
      timerOnOff = task.getTaskStatus().getTimerOnOff();
    }
  }

  @Getter
  static class CreateListsRequestDto {
    @NotNull(message = "날짜 범위는 필수입니다.")
    private LocalDateTime startTime;
    @NotNull(message = "날짜 범위는 필수입니다.")
    private LocalDateTime endTime;
  }

  @Getter
  static class DeleteRequestDto {

    private Long listsId;
  }
}
