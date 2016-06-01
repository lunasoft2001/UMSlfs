package at.ums.luna.umslfs.actividades;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import at.ums.luna.umslfs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class firmaFragment extends Fragment {

    public static final int SIGNATURE_ACTIVITY = 1;
    private Context esteContexto;

    public firmaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esteContexto = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firma, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button getSignature = (Button) getView().findViewById(R.id.signature);
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(esteContexto, CaptureSignature.class);
                startActivityForResult(intent,SIGNATURE_ACTIVITY);
            }
        });
    }


//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        switch(requestCode) {
//            case SIGNATURE_ACTIVITY:
//                if (resultCode == RESULT_OK) { //---->AQUI DA UN ERROR
//
//                    Bundle bundle = data.getExtras();
//                    String status  = bundle.getString("status");
//                    if(status.equalsIgnoreCase("done")){
//                        Toast toast = Toast.makeText(esteContexto, "Signature capture successful!", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.TOP, 105, 50);
//                        toast.show();
//                    }
//                }
//                break;
//        }
//
//    }
}
