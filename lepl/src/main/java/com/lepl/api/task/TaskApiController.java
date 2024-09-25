package com.lepl.api.task;

import static com.lepl.util.Messages.SUCCESS_TASK;
import static com.lepl.util.Messages.SUCCESS_TASK_DELETE;
import static com.lepl.util.Messages.SUCCESS_TASK_UPDATE;
import static com.lepl.util.Messages.VALID_TASK;

import com.lepl.Service.member.MemberService;
import com.lepl.Service.task.ListsService;
import com.lepl.Service.task.TaskService;
import com.lepl.Service.task.TaskStatusService;
import com.lepl.api.argumentresolver.Login;
import com.lepl.domain.member.Member;
import com.lepl.domain.task.Lists;
import com.lepl.domain.task.Task;
import com.lepl.domain.task.TaskStatus;
import com.lepl.util.ApiResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tasks")
public class TaskApiController {

  private final TaskService taskService;
  private final TaskStatusService taskStatusService;
  private final ListsService listsService;
  private final MemberService memberService;

  /**
   * create, delete, update
   */

  /**
   * 일정 추가 요청 형식(json) : CreateTaskRequestDto
   */
  @PostMapping(value = "/new")
  public ResponseEntity<ApiResponse<String>> create(@Login Long memberId,
      @RequestBody @Validated CreateTaskRequestDto request, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.info("검증 오류 발생 errors={}", bindingResult);
      ApiResponse res = ApiResponse.error(HttpStatus.BAD_REQUEST.value(), bindingResult);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
    Lists lists = listsService.findByCurrent(memberId,
        request.startTime); // db 에 startTime 인 lists 있는지 먼저 조회
    if (lists == null) { // null 인 경우 새로생성
      Member member = memberService.findOne(memberId);
      lists = Lists.createLists(member, request.startTime, new ArrayList<Task>());
    }

    TaskStatus taskStatus = TaskStatus.createTaskStatus(false, false);
    Task task = Task.createTask(request.content, request.startTime, request.endTime, taskStatus);
    lists.addTask(task); // 일정 추가

    listsService.join(lists);
    taskStatusService.join(taskStatus);
    taskService.join(task);
    ApiResponse res = ApiResponse.success(HttpStatus.CREATED.value(), SUCCESS_TASK);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  /**
   * 일정 삭제 요청 형식(json) : DeleteTaskRequestDto
   */
  @PostMapping(value = "/member/delete")
  public ResponseEntity<String> delete(@Login Long memberId,
      @RequestBody DeleteTaskRequestDto request) {
    Task task = taskService.findOneWithMember(memberId, request.getTaskId());
    if (task == null) {
      return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(VALID_TASK); // 208
    }
    taskService.remove(task);
    return ResponseEntity.status(HttpStatus.OK).body(SUCCESS_TASK_DELETE); // 200
  }

  /**
   * 일정 수정 요청 형식(json) : UpdateTaskRequestDto
   */
  @PostMapping(value = "/member/update")
  public ResponseEntity<ApiResponse<String>> update(@Login Long memberId,
      @RequestBody @Validated UpdateTaskRequestDto request, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.info("검증 오류 발생 errors={}", bindingResult);
      ApiResponse res = ApiResponse.error(HttpStatus.BAD_REQUEST.value(), bindingResult);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
    Task task = taskService.findOneWithMember(memberId, request.getTaskId());
    if (task == null) {
      ApiResponse res = ApiResponse.success(HttpStatus.ALREADY_REPORTED.value(), VALID_TASK);
      return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(res); // 208
    }
    taskService.update(task, request.content, request.startTime, request.endTime); // 변경 감지
    ApiResponse res = ApiResponse.success(HttpStatus.OK.value(), SUCCESS_TASK_UPDATE);
    return ResponseEntity.status(HttpStatus.OK).body(res); // 200
  }


  // DTO => 엔티티 외부노출 금지 + 필요한것만 담아서 반환할 수 있어서 효과적
  @Getter
  static class CreateTaskRequestDto {
    @Size(max = 100)
    private String content;
    @NotNull(message = "날짜 범위는 필수입니다.")
    private LocalDateTime startTime;
    @NotNull(message = "날짜 범위는 필수입니다.")
    private LocalDateTime endTime;
  }

  @Getter
  static class DeleteTaskRequestDto {

    private Long taskId;
  }

  @Getter
  static class UpdateTaskRequestDto {
    @NotNull
    private Long taskId;
    @Size(max = 100)
    private String content;
    @NotNull(message = "날짜 범위는 필수입니다.")
    private LocalDateTime startTime;
    @NotNull(message = "날짜 범위는 필수입니다.")
    private LocalDateTime endTime;
  }
}
