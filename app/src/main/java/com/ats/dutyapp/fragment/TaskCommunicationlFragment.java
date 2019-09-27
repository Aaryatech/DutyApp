package com.ats.dutyapp.fragment;


import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;

import com.ats.dutyapp.R;
import com.ats.dutyapp.adapter.ChatArrayAdapter;
import com.ats.dutyapp.model.ChatMessage;
import com.ats.dutyapp.model.ChatTask;
import com.ats.dutyapp.model.Login;
import com.ats.dutyapp.utils.CustomSharedPreference;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskCommunicationlFragment extends Fragment {
public EditText edEnterText;
public ImageView ivCamera,imageViewPlay;
public Button btnAudio,btnSend;
public Chronometer pai_cron;
public SeekBar seekBar;
//public RecyclerView recyclerView;
public ListView listView;
private ChatArrayAdapter chatArrayAdapter;
private boolean side = false;
public static Login loginUser;
public static ChatTask model;

//--------------------------------------------------Audio Recording-----------------------------
private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    MediaRecorder mRecorder ;
    MediaPlayer mPlayer ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_task_communicationl, container, false);
        getActivity().setTitle("Task Communication");

        edEnterText=(EditText)view.findViewById(R.id.edEnterText);
        ivCamera=(ImageView)view.findViewById(R.id.ivCamera);
        btnAudio=(Button)view.findViewById(R.id.btnAudio);
        btnSend=(Button)view.findViewById(R.id.btnSend);
        pai_cron = (Chronometer)view.findViewById(R.id.pai_cron);

        listView=(ListView) view.findViewById(R.id.listView);


        try {
            String userStr = CustomSharedPreference.getString(getActivity(), CustomSharedPreference.MAIN_KEY_USER);
            Gson gson = new Gson();
            loginUser = gson.fromJson(userStr, Login.class);
            Log.e("LOGIN USER MAIN : ", "--------USER-------" + loginUser);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
            String quoteStr = getArguments().getString("model");
            Gson gson = new Gson();
            model = gson.fromJson(quoteStr, ChatTask.class);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        chatArrayAdapter = new ChatArrayAdapter(getActivity(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        edEnterText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        Log.e("Adapter","------------------------------------------------"+chatArrayAdapter.toString());

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        edEnterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 ) {
                    btnSend.setVisibility(View.VISIBLE);
                    btnAudio.setVisibility(View.GONE);
                } else {
                    btnSend.setVisibility(View.GONE);
                    btnAudio.setVisibility(View.VISIBLE);
                }
            }
        });

        btnAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                       // AppLog.logString("Start Recording");
                        Log.e("Start","-------------------------------------------------------------Start Recording---------------------");
                       startRecording();
                        return true;
                    case MotionEvent.ACTION_UP:
                       // AppLog.logString("stop Recording");
                        Log.e("Stop","-------------------------------------------------------------Stop Recording------------------");
                       stopRecording();
                        break;
                }
                return false;
            }
        });

        return view;
    }

    private void stopRecording() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlay.setImageResource(R.drawable.ic_camera);
        pai_cron.stop();
    }

    private void startRecording() {
        Log.e("Start","-------------------------------------------------------------");
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName = root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" +
                String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename", fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
       // seekBar.setProgress(0);
       // stopPlaying();
        //starting the chronometer
        pai_cron.setBase(SystemClock.elapsedRealtime());
        pai_cron.start();

        Log.e("End","-------------------------------------------------------------");

    }

    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
       // imageViewPlay.setImageResource(R.drawable.ic_camera);
        pai_cron.stop();
    }

    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side, edEnterText.getText().toString()));
        edEnterText.setText("");
        side = !side;
        Log.e("Side","-------------------------------------"+side);
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_abouttask);
        item.setVisible(true);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_abouttask:
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, new AboutTaskFragment(), "TaskCommunicationListFragment");
//                ft.commit();

                Gson gson = new Gson();
                String json = gson.toJson(model);
                String json1 = gson.toJson(loginUser);

                Fragment adf = new AboutTaskFragment();
                Bundle args = new Bundle();
                args.putString("model", json);
                args.putString("login", json1);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "TaskCommunicationListFragment").commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


}
