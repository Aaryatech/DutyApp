package com.ats.dutyapp.interfaces;

import com.ats.dutyapp.model.AssignDetail;
import com.ats.dutyapp.model.AssignDuty;
import com.ats.dutyapp.model.DeptCount;
import com.ats.dutyapp.model.DutyDetail;
import com.ats.dutyapp.model.DutyHeader;
import com.ats.dutyapp.model.DutyHeaderDetail;
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
}
