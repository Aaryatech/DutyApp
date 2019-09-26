package com.ats.dutyapp.interfaces;

import com.ats.dutyapp.model.ActionHeaderClose;
import com.ats.dutyapp.model.AssignChecklist;
import com.ats.dutyapp.model.AssignDetail;
import com.ats.dutyapp.model.AssignDuty;
import com.ats.dutyapp.model.ChatGroup;
import com.ats.dutyapp.model.ChatHeader;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.ChecklistActionHeader;
import com.ats.dutyapp.model.Detail;
import com.ats.dutyapp.model.GroupList;
import com.ats.dutyapp.model.SaveAssigneChecklist;
import com.ats.dutyapp.model.ChecklistHeader;
import com.ats.dutyapp.model.Department;
import com.ats.dutyapp.model.DeptCount;
import com.ats.dutyapp.model.Document;
import com.ats.dutyapp.model.DutyDetail;
import com.ats.dutyapp.model.DutyDone;
import com.ats.dutyapp.model.DutyHeader;
import com.ats.dutyapp.model.DutyHeaderDetail;
import com.ats.dutyapp.model.Emp;
import com.ats.dutyapp.model.EmpCount;
import com.ats.dutyapp.model.Employee;
import com.ats.dutyapp.model.Info;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.model.Sync;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface InterfaceAPI {

    @POST("master/allEmployeesByDept")
    Call<ArrayList<Employee>> allEmployeesByDept(@Query("deptId") ArrayList<Integer> deptId);

    @POST("master/login")
    Call<Login> doLogin(@Query("dscNumber") String dscNumber);

    @POST("master/updateToken")
    Call<Info> updateUserToken(@Query("empId") int empId, @Query("token") String token);

    @POST("duty/master/getTaskDoneHeaderByEmpAndDate")
    Call<ArrayList<DutyHeader>> getTaskDoneHeaderByEmpAndDate(@Query("empId") int empId,@Query("date") String date);

    @POST("duty/master/getTaskDoneDetailByHeaderId")
    Call<ArrayList<DutyDetail>> getTaskDoneDetailByHeaderId(@Query("headerId") int headerId);

    @POST("duty/master/updateTaskStatus")
    Call<Info> updateTaskStatus(@Query("detailId") int detailId, @Query("headerId") int headerId,@Query("photoReq") int photoReq,@Query("remarkReq") int remarkReq,@Query("photo1") String photo1,@Query("photo2") String photo2,@Query("photo3") String photo3,@Query("photo4") String photo4, @Query("photo5") String photo5,@Query("remark") String remark,@Query("status") int status);

    @GET("duty/master/allDutyHeaderDetailByDept")
    Call<ArrayList<DutyHeaderDetail>> allDutyHeaderDetailByDept(@Query("deptId") ArrayList<Integer> deptId);

    @POST("duty/master/saveAssignDuty")
    Call<AssignDuty> saveAssignDuty(@Body AssignDuty assignDuty);

    @POST("duty/master/getAssignDutyByDutyId")
    Call<AssignDetail> getAssignDutyByDutyId(@Query("dutyId") int dutyId);

    @POST("duty/master/updateAssignDutyTimeAndEmpIds")
    Call<Info> updateAssignDutyTimeAndEmpIds(@Query("assignId") int assignId,@Query("notifyTime") String notifyTime,@Query("empIds") String empIds);

    @Multipart
    @POST("photoUpload")
    Call<JSONObject> imageUpload(@Part MultipartBody.Part[] filePath, @Part("imageName") ArrayList<String> name, @Part("type") RequestBody type);

    @GET("master/allSettings")
    Call<ArrayList<Sync>> allSettings();

    @POST("duty/master/getDeptWiseCount")
    Call<ArrayList<DeptCount>> getDeptWiseCount(@Query("deptId") ArrayList<Integer> deptId,@Query("date") String date);

    @POST("duty/master/getEmpWiseCount")
    Call<ArrayList<EmpCount>> getEmpWiseCount(@Query("deptId") int deptId,@Query("empId") ArrayList<Integer> empId, @Query("date") String date);

    @POST("duty/master/updateAssignDutySchedule")
    Call<Info> updateAssignDutySchedule(@Query("assignId") int assignId, @Query("status") int status);

    @POST("duty/master/getDutyReportByEmp")
    Call<ArrayList<Document>> getDutyReportByEmp(@Query("empId") int empId,@Query("langId") int langId);

    @POST("duty/master/saveTaskDoneHeader")
    Call<DutyDone> saveTaskDoneHeader(@Body DutyDone dutyDone);

    @GET("master/allEmployeeDepartment")
    Call<ArrayList<Department>> allEmployeeDepartment();

    @GET("master/allEmployeesByDesg")
    Call<ArrayList<Emp>> allEmployees();

    @POST("checklist/saveChecklistHeaderAndDetail")
    Call<ChecklistHeader> saveChecklistHeaderAndDetail(@Body ChecklistHeader checklistHeader);

    @GET("checklist/allChecklistHeader")
    Call<ArrayList<ChecklistHeader>> allChecklistHeader();

    @POST("checklist/getAllChecklistHeaderAndDetail")
    Call<ArrayList<ChecklistHeader>> getAllChecklistHeaderAndDetail();

    @GET("checklist/getAllAssignedChecklistDisplay")
    Call<ArrayList<AssignChecklist>> getAllAssignedChecklistDisplay();

    @POST("checklist/deleteChecklistAssign")
    Call<Info> deleteChecklistAssign(@Query("assignId") int assignId);

    @POST("checklist/saveChecklistAssign")
    Call<SaveAssigneChecklist> saveChecklistAssign(@Body SaveAssigneChecklist assigneSave);

    @POST("checklist/getOpenChecklistByEmp")
    Call<ArrayList<Detail>> getOpenChecklistByEmp(@Query("empId") int empId);

    @POST("checklist/getAllChecklistByDept")
    Call<ArrayList<ChecklistHeader>> getAllChecklistByDept(@Query("deptId") int deptId);

    @POST("checklist/saveChecklistActionHeaderAndDetail")
    Call<ChecklistActionHeader> saveChecklistActionHeaderAndDetail(@Body ChecklistActionHeader checklistActionHeader);

    @POST("checklist/getClosedChecklistByEmp")
    Call<ArrayList<ActionHeaderClose>> getClosedChecklistByEmp(@Query("empId") int empId);

    @POST("checklist/updateClosedChecklistDetailStatus")
    Call<Info> updateClosedChecklistDetailStatus(@Query("actionDetailId") int actionDetailId,@Query("status") int status,@Query("photo") String photo);

    @POST("checklist/updateClosedChecklistDetailPhoto")
    Call<Info> updateClosedChecklistDetailPhoto(@Query("actionDetailId") int actionDetailId,@Query("photo") String photo);

    @POST("checklist/updateClosedChecklistDetailStatusMultiple")
    Call<Info> updateClosedChecklistDetailStatusMultiple(@Query("actionDetailId")  ArrayList<Integer>  actionDetailId,@Query("status") int status);

    @POST("checklist/updateClosedChecklistHeaderStatus")
    Call<Info> updateClosedChecklistHeaderStatus(@Query("actionHeaderId") int actionHeaderId,@Query("status") int status,@Query("empId") int empId);

    @POST("checklist/deleteChecklistHeader")
    Call<Info> deleteChecklistHeader(@Query("checklistHeaderId") int checklistHeaderId);

    @POST("chat/saveChatGroup")
    Call<ChatGroup> saveChatGroup(@Body ChatGroup chatGroup);

    @POST("chat/getAllChatGroupDisplay")
    Call<ArrayList<GroupList>> getAllChatGroupDisplay(@Query("isActive") int isActive);

    @POST("chat/deleteChatGroup")
    Call<Info> deleteChatGroup(@Query("groupId") int groupId);

    @POST("chat/getAllChatHeaderDisplay")
    Call<ArrayList<ChatTask>> getAllChatHeaderDisplay(@Query("isActive") int isActive);

    @POST("chat/deleteChatHeader")
    Call<Info> deleteChatHeader(@Query("headerId") int headerId);

    @POST("chat/saveChatHeader")
    Call<ChatHeader> saveChatHeader(@Body ChatHeader chatHeader);

}
