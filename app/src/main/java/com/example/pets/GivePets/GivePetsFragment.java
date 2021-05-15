package com.example.pets.GivePets;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pets.R;
import com.example.pets.screen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class GivePetsFragment extends Fragment {
    private Button account;
    private Button buy;

    private EditText amount;
    private EditText description;
    private Button submit;
    private ImageView imageView;
    private TextView tran;
    private TextView tran2;
    private CropImageView cropImageView;

    private NumberPicker numberPicker;

    private ImageButton info;



    String userID;
    String pets;

    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    private Uri imageUri;
    private StorageReference storageReference;
    FirebaseStorage storage;
    public String bookID;
    TextView uid;
    private boolean flag=false;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment_give, container, false);

        numberPicker= view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(171);
        info= view.findViewById(R.id.info_button);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("List of breeds and their respective number")
                        .setMessage("1\taffenpinscher\n" +
                                "2\tafrican\n" +
                                "3\tairedale\n" +
                                "4\takita\n" +
                                "5\tappenzeller\n" +
                                "6\taustralian\n" +
                                "7\tshepherd\n" +
                                "8\tbasenji\n" +
                                "9\tbeagle\n" +
                                "10\tbluetick\n" +
                                "11\tborzoi\n" +
                                "12\tbouvier\n" +
                                "13\tboxer\n" +
                                "14\tbrabancon\n" +
                                "15\tbriard\n" +
                                "16\tbuhund\n" +
                                "17\tnorwegian\n" +
                                "18\tbulldog\n" +
                                "19\tboston\n" +
                                "20\tenglish\n" +
                                "21\tfrench\n" +
                                "22\tbullterrier\n" +
                                "23\tstaffordshire\n" +
                                "24\tcairn\n" +
                                "25\tcattledog\n" +
                                "26\taustralian\n" +
                                "27\tchihuahua\n" +
                                "28\tchow\n" +
                                "29\tclumber\n" +
                                "30\tcockapoo\n" +
                                "31\tcollie\n" +
                                "32\tborder\n" +
                                "33\tcoonhound\n" +
                                "34\tcorgi\n" +
                                "35\tcardigan\n" +
                                "36\tcotondetulear\n" +
                                "37\tdachshund\n" +
                                "38\tdalmatian\n" +
                                "39\tdane\n" +
                                "40\tgreat\n" +
                                "41\tdeerhound\n" +
                                "42\tscottish\n" +
                                "43\tdhole\n" +
                                "44\tdingo\n" +
                                "45\tdoberman\n" +
                                "46\telkhound\n" +
                                "47\tnorwegian\n" +
                                "48\tentlebucher\n" +
                                "49\teskimo\n" +
                                "50\tfinnish\n" +
                                "51\tlapphund\n" +
                                "52\tfrise\n" +
                                "53\tbichon\n" +
                                "54\tgermanshepherd\n" +
                                "55\tgreyhound\n" +
                                "56\titalian\n" +
                                "57\tgroenendael\n" +
                                "58\thavanese\n" +
                                "59\thound\n" +
                                "60\tafghan\n" +
                                "61\tbasset\n" +
                                "62\tblood\n" +
                                "63\tenglish\n" +
                                "64\tibizan\n" +
                                "65\tplott\n" +
                                "66\twalker\n" +
                                "67\thusky\n" +
                                "68\tkeeshond\n" +
                                "69\tkelpie\n" +
                                "70\tkomondor\n" +
                                "71\tkuvasz\n" +
                                "72\tlabradoodle\n" +
                                "73\tlabrador\n" +
                                "74\tleonberg\n" +
                                "75\tlhasa\n" +
                                "76\tmalamute\n" +
                                "77\tmalinois\n" +
                                "78\tmaltese\n" +
                                "79\tmastiff\n" +
                                "80\tbull\n" +
                                "81\tenglish\n" +
                                "82\ttibetan\n" +
                                "83\tmexicanhairless\n" +
                                "84\tmix\n" +
                                "85\tmountain\n" +
                                "86\tbernese\n" +
                                "87\tswiss\n" +
                                "88\tnewfoundland\n" +
                                "89\totterhound\n" +
                                "90\tovcharka\n" +
                                "91\tcaucasian\n" +
                                "92\tpapillon\n" +
                                "93\tpekinese\n" +
                                "94\tpembroke\n" +
                                "95\tpinscher\n" +
                                "96\tminiature\n" +
                                "97\tpitbull\n" +
                                "98\tpointer\n" +
                                "99\tgerman\n" +
                                "100\tgermanlonghair\n" +
                                "101\tpomeranian\n" +
                                "102\tpoodle\n" +
                                "103\tminiature\n" +
                                "104\tstandard\n" +
                                "105\ttoy\n" +
                                "106\tpug\n" +
                                "107\tpuggle\n" +
                                "108\tpyrenees\n" +
                                "109\tredbone\n" +
                                "110\tretriever\n" +
                                "111\tchesapeake\n" +
                                "112\tcurly\n" +
                                "113\tflatcoated\n" +
                                "114\tgolden\n" +
                                "115\tridgeback\n" +
                                "116\trhodesian\n" +
                                "117\trottweiler\n" +
                                "118\tsaluki\n" +
                                "119\tsamoyed\n" +
                                "120\tschipperke\n" +
                                "121\tschnauzer\n" +
                                "122\tgiant\n" +
                                "123\tminiature\n" +
                                "124\tsetter\n" +
                                "125\tenglish\n" +
                                "126\tgordon\n" +
                                "127\tirish\n" +
                                "128\tsheepdog\n" +
                                "129\tenglish\n" +
                                "130\tshetland\n" +
                                "131\tshiba\n" +
                                "132\tshihtzu\n" +
                                "133\tspaniel\n" +
                                "134\tblenheim\n" +
                                "135\tbrittany\n" +
                                "136\tcocker\n" +
                                "137\tirish\n" +
                                "138\tjapanese\n" +
                                "139\tsussex\n" +
                                "140\twelsh\n" +
                                "141\tspringer\n" +
                                "142\tenglish\n" +
                                "143\tstbernard\n" +
                                "144\tterrier\n" +
                                "145\tamerican\n" +
                                "146\taustralian\n" +
                                "147\tbedlington\n" +
                                "148\tborder\n" +
                                "149\tdandie\n" +
                                "150\tfox\n" +
                                "151\tirish\n" +
                                "152\tkerryblue\n" +
                                "153\tlakeland\n" +
                                "154\tnorfolk\n" +
                                "155\tnorwich\n" +
                                "156\tpatterdale\n" +
                                "157\trussell\n" +
                                "158\tscottish\n" +
                                "159\tsealyham\n" +
                                "160\tsilky\n" +
                                "161\ttibetan\n" +
                                "162\ttoy\n" +
                                "163\twesthighland\n" +
                                "164\twheaten\n" +
                                "165\tyorkshire\n" +
                                "166\tvizsla\n" +
                                "167\twaterdog\n" +
                                "168\tspanish\n" +
                                "169\tweimaraner\n" +
                                "170\twhippet\n" +
                                "171\twolfhound\n" +
                                "171\tirish\n").setNeutralButton("Ok", null)
                        .show();
            }
        });

        final String bookID= UUID.randomUUID().toString();
        amount= view.findViewById(R.id.amount);
        description= view.findViewById(R.id.description);
        uid= (TextView) view.findViewById(R.id.uid);
        imageView= (ImageView) view.findViewById(R.id.imageView3);
        submit= view.findViewById(R.id.submit);
        tran= view.findViewById(R.id.tran);
        tran2= view.findViewById(R.id.tran2);

        Animation animation= AnimationUtils.loadAnimation(getContext(), R.anim.text_view_anim);
        tran.startAnimation(animation);
        tran2.startAnimation(animation);

        uid.setText(bookID);
        storage= FirebaseStorage.getInstance();
        storageReference= storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        userID=mAuth.getCurrentUser().getUid();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String AMOUNT= amount.getText().toString().trim();
                final String DESCRIPTION= description.getText().toString();
                final String MOBILE= screen.number;

                if (TextUtils.isEmpty(AMOUNT))
                {
                    amount.setError("Please enter amount");
                    return;
                }
                if (TextUtils.isEmpty(DESCRIPTION))
                {
                    description.setError("Please enter description");
                    return;
                }

                if (flag)
                {
                    uploadPicture();
                }
                else {
                    Snackbar.make(view, "Choose a picture!!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                userID = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference= fstore.collection("pets").document(bookID);
                Map<String, Object> user= new HashMap<>();
                user.put("amount", AMOUNT);
                user.put("description", DESCRIPTION);
                user.put("city", screen.city);
                user.put("mobile", screen.number);
                user.put("breed", String.valueOf(numberPicker.getValue()));

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "SUBMITTED", Toast.LENGTH_LONG).show();
                        Toast toast = Toast.makeText(getContext(),
                                "Book added successfully",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });
                final DocumentReference documentRef= fstore.collection("users").document(userID);
                documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override

                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        pets = documentSnapshot.getString("pets");
                        Map<String, Object> object= new HashMap<>();
                        String temp= pets + "," + bookID;
                        if (pets ==null)
                        {
                            object.put("pets", bookID);
                        }
                        else
                        {
                            object.put("pets", temp);
                        }
                        documentRef.update(object);
                    }
                });

            }
        });
        
        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri= result.getUri();
                imageView.setImageURI(imageUri);
                flag=true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void uploadPicture() {
        final ProgressDialog pd= new ProgressDialog(getContext());
        pd.setTitle("Image is being uploaded!");
        pd.show();
        StorageReference riversRef = storageReference.child("images/" + uid.getText().toString());

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Upload Succesful", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "" + "Please make sure that the " +
                                "image is less than 1024 KB/ 1 MB", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent= (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int) progressPercent + "%");
                    }
                });
    }

    private void choosePicture() {
        /*Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);*/
        CropImage.activity()
                .setAspectRatio(259, 259)
                .start(getContext(), this);
    }

}
