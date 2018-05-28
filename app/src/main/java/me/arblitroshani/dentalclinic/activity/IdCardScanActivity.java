package me.arblitroshani.dentalclinic.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.microblink.activity.ScanActivity;
import com.microblink.activity.ScanCard;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkid.mrtd.MRTDRecognitionResult;
import com.microblink.recognizers.blinkid.mrtd.MRTDRecognizerSettings;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.results.ocr.OcrResult;
import com.microblink.util.RecognizerCompatibility;
import com.microblink.util.RecognizerCompatibilityStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.extra.Config;
import me.arblitroshani.dentalclinic.model.User;

public class IdCardScanActivity extends AppCompatActivity {

    public static final int MY_REQUEST_CODE = 0x101;
    private static final int CAMERA_DISTANCE = 8000;

    private boolean mIsBackVisible = false;

    @BindView(R.id.flIdCardFront)
    View id_front;
    @BindView(R.id.flIdCardBack)
    View id_back;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AnimatorSet setRightOut;
    private AnimatorSet setLeftIn;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_scan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Animation
        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);

        float scale = getResources().getDisplayMetrics().density * CAMERA_DISTANCE;
        id_front.setCameraDistance(scale);
        id_back.setCameraDistance(scale);

        // Flip after 2 seconds
        final Handler handler = new Handler();
        handler.postDelayed(this::flip, 1000);

        // ScanActivity
        // check if BlinkID is supported on the device
        RecognizerCompatibilityStatus supportStatus = RecognizerCompatibility.getRecognizerCompatibilityStatus(this);
        if (supportStatus != RecognizerCompatibilityStatus.RECOGNIZER_SUPPORTED) {
            Toast.makeText(this, "BlinkID is not supported! Reason: " + supportStatus.name(), Toast.LENGTH_LONG).show();
        }

        final Intent intent = new Intent(this, ScanCard.class);
        intent.putExtra(ScanCard.EXTRAS_LICENSE_KEY, Config.BLINKID_LICENSE_KEY);

        RecognitionSettings settings = new RecognitionSettings();
        settings.setRecognizerSettingsArray(setupSettingsArray());
        intent.putExtra(ScanCard.EXTRAS_RECOGNITION_SETTINGS, settings);
        intent.putExtra(ScanActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        fab.setOnClickListener(view -> startActivityForResult(intent, MY_REQUEST_CODE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == ScanCard.RESULT_OK && data != null) {
                RecognitionResults result = data.getParcelableExtra(ScanCard.EXTRAS_RECOGNITION_RESULTS);
                onScanningDone(result);
            }
        }
    }

    private void onScanningDone(RecognitionResults results) {
        BaseRecognitionResult[] dataArray = results.getRecognitionResults();

        for (BaseRecognitionResult baseResult : dataArray) {
            if (baseResult instanceof MRTDRecognitionResult) {
                MRTDRecognitionResult result = (MRTDRecognitionResult) baseResult;

                if(result.isValid() && !result.isEmpty()) {
                    if(result.isMRZParsed()) {
                        // read data
                        User incompleteUser = new User(
                                result.getSecondaryId(),
                                result.getPrimaryId(),
                                "no_email",
                                "no_phone",
                                getBirthday(result.getRawDateOfBirth()),
                                result.getSex(),
                                result.getDocumentNumber(),
                                user.getUid(),
                                result.getNationality(),
                                User.TYPE_USER
                        );

                        String nationalId = result.getOpt1().substring(0, 10);

                        // pass it in intent
                        Intent intent = new Intent(this, CreateUserProfileActivity.class);
                        intent.putExtra("incomplete_user", incompleteUser);
                        intent.putExtra("incomplete_user_national_id", nationalId);
                        startActivity(intent);
                        finish();
                    } else {
                        OcrResult rawOcr = result.getOcrResult();
                        // attempt to parse OCR result by yourself
                        // or ask user to try again
                    }
                } else {
                    // not all relevant data was scanned, ask user
                    // to try again
                }
            }
        }
    }

    private String getBirthday(String rawDateOfBirth) {
        return rawDateOfBirth.substring(2, 4) + '/' +
                rawDateOfBirth.substring(4) + '/' +
                rawDateOfBirth.substring(0, 2);
    }

    public void flip() {
        if (!mIsBackVisible) {
            setRightOut.setTarget(id_front);
            setLeftIn.setTarget(id_back);
        } else {
            setRightOut.setTarget(id_back);
            setLeftIn.setTarget(id_front);
        }
        setRightOut.start();
        setLeftIn.start();
        mIsBackVisible = !mIsBackVisible;
    }

    public void flip(View view) {
        flip();
    }

    private RecognizerSettings[] setupSettingsArray() {
        MRTDRecognizerSettings sett = new MRTDRecognizerSettings();
        return new RecognizerSettings[] { sett };
    }



}
