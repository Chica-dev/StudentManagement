package reisetech.student.management.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import reisetech.student.management.data.ApplicationStatus;
import reisetech.student.management.exception.InvalidStatusTransitionException;
import reisetech.student.management.service.ApplicationStatusService;
import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(ApplicationStatusController.class)
class ApplicationStatusControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JsonMapper jsonMapper;

  @MockitoBean
  private ApplicationStatusService service;

  private ApplicationStatus createValidApplicationStatus() {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setId(1);
    applicationStatus.setStudentsCoursesId(1);
    applicationStatus.setStatus("本申込");
    return applicationStatus;
  }

  @Test
  void 申込状況の一覧検索が実行できて空のリストが返ってくること() throws  Exception {
    when(service.searchApplicationStatusList()).thenReturn(List.of());

    mockMvc.perform(get("/applicationStatusList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchApplicationStatusList();
  }

  @Test
  void 申込状況の一覧検索が実行できて申込状況のリストが返ってくること() throws Exception {
    ApplicationStatus applicationStatus = createValidApplicationStatus();

    when(service.searchApplicationStatusList()).thenReturn(List.of(applicationStatus));

    mockMvc.perform(get("/applicationStatusList"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].status").value("本申込"));

    verify(service, times(1)).searchApplicationStatusList();
  }

  @Test
  void 受講生コース情報IDに紐づく申込状況の検索が正常に実行できること() throws Exception {
    ApplicationStatus applicationStatus = createValidApplicationStatus();

    when(service.searchApplicationStatus(1)).thenReturn(applicationStatus);

    mockMvc.perform(get("/applicationStatus").param("studentsCoursesId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("本申込"));

    verify(service, times(1)).searchApplicationStatus(1);
  }

  @Test
  void 申込状況の登録が正常に実行できること() throws Exception {
    ApplicationStatus applicationStatus = new ApplicationStatus();
    applicationStatus.setId(1);
    applicationStatus.setStudentsCoursesId(1);
    applicationStatus.setStatus("仮申込");

    when(service.registerApplicationStatus(1)).thenReturn(applicationStatus);

    mockMvc.perform(post("/registerApplicationStatus").param("studentsCoursesId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("仮申込"));

    verify(service, times(1)).registerApplicationStatus(1);
  }

  @Test
  void 申込状況の更新が正常に実行できること() throws Exception {
    ApplicationStatus applicationStatus = createValidApplicationStatus();

    mockMvc.perform(put("/updateApplicationStatus")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonMapper.writeValueAsString(applicationStatus)))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateApplicationStatus(any(ApplicationStatus.class));
  }

  @Test
  void 申込状況の更新で状態が未入力の時に入力チェックに掛かること() throws Exception {
    ApplicationStatus applicationStatus = createValidApplicationStatus();
    applicationStatus.setStatus("");

    mockMvc.perform(put("/updateApplicationStatus")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonMapper.writeValueAsString(applicationStatus)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$['status']").value("申込状況は必須です"));

    verify(service, times(0)).updateApplicationStatus(any(ApplicationStatus.class));
  }

  @Test
  void 申込状況の更新で不正な状態遷移の場合に400が返ること() throws Exception {
    ApplicationStatus applicationStatus = createValidApplicationStatus();

    doThrow(new InvalidStatusTransitionException("本申込から仮申込への変更はできません。"))
        .when(service).updateApplicationStatus(any(ApplicationStatus.class));

    mockMvc.perform(put("/updateApplicationStatus")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonMapper.writeValueAsString(applicationStatus)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("本申込から仮申込への変更はできません。"));

    verify(service, times(1)).updateApplicationStatus(any(ApplicationStatus.class));
  }
}