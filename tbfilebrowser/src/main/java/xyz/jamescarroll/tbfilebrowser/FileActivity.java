package xyz.jamescarroll.tbfilebrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FileActivity extends AppCompatActivity {
    public static final String FORCE_LIST_VIEW = FileActivity.class.getName() + ".FORCE_LIST_VIEW";
    public static final String EXTRA_BASE_PATH = FileActivity.class.getName() + ".EXTRA_BASE_PATH";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
    }
}
