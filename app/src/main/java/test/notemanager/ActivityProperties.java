package test.notemanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;

import test.notemanager.models.Note;
import test.notemanager.models.Priorities;
import test.notemanager.models.Priority;
import test.notemanager.sqlite.NotesHelper;

public class ActivityProperties extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    @Override
    public void onConnected(Bundle bundle) {
        if (mNote.getLocation() != null) {
            mCurrentLocation = new Location("Current location");
            mCurrentLocation.setLatitude(mNote.getLocation().latitude);
            mCurrentLocation.setLongitude(mNote.getLocation().longitude);
        }
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
//            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//            Criteria criteria = new Criteria();
//            String provider = locationManager.getBestProvider(criteria, true);
//            Location location = locationManager.getLastKnownLocation(provider);
//            if (location != null) {
//                mCurrentLocation = new Location("current_location");
//                mCurrentLocation.setLatitude(location.getLatitude());
//                mCurrentLocation.setLongitude(location.getLongitude());
//            }
        }
        if (mCurrentLocation != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))
                    .draggable(true));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 15f));

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        Log.d("logs", "googleapi connected");
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private Uri imageUri;
    private String mCurrentPhotoPath;

    private Note mNote;
    private NotesHelper mHelper;
    private ImageView mPriorityImage;
    private ImageView mPhotoImage;
    private CheckBox mPhotoCheck;
    private CheckBox mGPSCheck;
    private int PICK_IMAGE_REQUEST = 1;
    private int TAKE_PHOTO_REQUEST = 0;

    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private GoogleMap googleMap;

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getIntent().getExtras().putSerializable("mNote", mNote);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO_REQUEST) {
                try {
//                    Bitmap b = Picasso.with(this).load(imageUri).get();
                    mPhotoImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == PICK_IMAGE_REQUEST) {
                Uri selectedImage = data.getData();
                imageUri = selectedImage;
                try {
                    mPhotoImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


    }

    @Override
    public void onBackPressed() {
        backOption();
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new NotesHelper(this);
        mNote = (Note) getIntent().getExtras().getSerializable("mNote");
        setContentView(R.layout.activity_properties);

        final ScrollView mainScrollView = (ScrollView) findViewById(R.id.main_scrollview);
        ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    case MotionEvent.ACTION_UP:
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        final SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        fm.getMapAsync(new OnMapReadyCallback() {
            public void onMapReady(GoogleMap map) {
                if (map != null) {
                    googleMap = map;
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setRotateGesturesEnabled(true);
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            mCurrentLocation = new Location("Current location");
                            mCurrentLocation.setLatitude(latLng.latitude);
                            mCurrentLocation.setLongitude(latLng.longitude);

                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .draggable(true));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    latLng, 15f));

                        }
                    });
                }
            }
        });
        mPriorityImage = (ImageView) findViewById(R.id.priority_image);
        mPhotoImage = (ImageView) findViewById(R.id.photo_image);
        mPhotoCheck = (CheckBox) findViewById(R.id.photo_description);
        mGPSCheck = (CheckBox) findViewById(R.id.google_map_description);
        setTitle(mNote.getHead().toString());

        if (mNote.getImageUri() == null) {
            mPhotoImage.setVisibility(View.GONE);
            mPhotoCheck.setChecked(false);
        } else {
            mPhotoImage.setVisibility(View.VISIBLE);
            mPhotoCheck.setChecked(true);
            imageUri = mNote.getImageUri();
            try {

                mPhotoImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), mNote.getImageUri()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (mNote.getLocation() == null) {
            mGPSCheck.setChecked(false);
            fm.getView().setVisibility(View.GONE);
        } else {
            mGPSCheck.setChecked(true);
            fm.getView().setVisibility(View.VISIBLE);

        }
        mPhotoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) mPhotoImage.setVisibility(View.VISIBLE);
                else mPhotoImage.setVisibility(View.GONE);
            }
        });

        mGPSCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) fm.getView().setVisibility(View.VISIBLE);
                else fm.getView().setVisibility(View.GONE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPriorityImage.setImageResource(Priorities.getEnumInstance().get(mNote.getPriority()));


        mPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(imageUri, "image/*");
                    startActivity(intent);
                }
            }

        });
        mPhotoImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                updatePhotoOption();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_properties, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_back:
                backOption();
                break;
            case R.id.menu_save_props:
                savePropsOption();
                break;
            case R.id.menu_update_photo:
                if (mPhotoCheck.isChecked())
                    updatePhotoOption();
                break;
            case R.id.menu_rename_head_props:
                renameHeadOption();
                break;
            case R.id.menu_choose_priority:
                choosePriorityOption();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void updatePhotoOption() {
        final CharSequence[] items = {"Сделать снимок",/* "Выбрать из галереи",*/
                "Отмена"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выбрать фото");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Сделать снимок")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                        }
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                            imageUri = Uri.fromFile(photoFile);
                            startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST);
                        }
                    }
//                } else if (items[item].equals("Выбрать из галереи")) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                } else if (items[item].equals("Отмена")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void renameHeadOption() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.input_head_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit_text_head);
        dialogBuilder.setTitle("Введие заголовок");
        dialogBuilder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!edt.getText().toString().equals(""))
                    setTitle(edt.getText().toString());
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void choosePriorityOption() {
        final CharSequence[] items = {"Высокий", "Средний", "Низкий", "Отсутствует", "Отмена"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выбрать фото");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Высокий")) {
                    mNote.setPriority(Priority.HIGH_PRIOITY);
                } else if (items[item].equals("Средний")) {
                    mNote.setPriority(Priority.MEDIUM_PRIORITY);
                } else if (items[item].equals("Низкий")) {
                    mNote.setPriority(Priority.LOW_PRIORITY);
                } else if (items[item].equals("Отсутствует")) {
                    mNote.setPriority(Priority.NO_PRIORITY);
                } else if (items[item].equals("Отмена")) {
                    dialog.dismiss();
                }
                mPriorityImage.setImageResource(
                        Priorities.getEnumInstance()
                                .get(mNote.getPriority()));
            }
        });
        builder.show();

    }


    private void savePropsOption() {
        mNote.setHead(getTitle().toString());
        mNote.setLocation(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        if (imageUri != null)
            mNote.setImageUri(imageUri);
        mNote = mHelper.saveNote(mNote);
    }


    private void backOption() {
        Intent result = new Intent();
        Bundle resultData = new Bundle();
        resultData.putSerializable("mNote", mNote);
        result.putExtras(resultData);
        setResult(Activity.RESULT_OK, result);
        finish();
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    private File createImageFile() throws IOException {
        String imageFileName = "sample" + mNote.getId();
        File root = new File(Environment.getExternalStorageDirectory() + File.separator +
                Environment.DIRECTORY_DCIM + File.separator + "NoteManager", "Notes");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                root      /* directory */
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
