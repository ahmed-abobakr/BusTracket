package arab_open_university.com.bustracker;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

/**
 * Created by akhalaf on 5/3/2017.
 */

public class StartTipDialog extends DialogFragment {


    Spinner spinnerFrom;
    Button btnSubmit;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_trip_dialog, container, false);
        spinnerFrom = (Spinner) view.findViewById(R.id.from_spinner);
        btnSubmit = (Button) view.findViewById(R.id.submit);
        List<String> stationsList = getArguments().getStringArrayList("stations_list");
        spinnerFrom.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, stationsList));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinnerFrom.getSelectedItemPosition() == 0)
                    BusTrackerApp.onStartedStationChoosen.onStartedStationChoosen(false);
                else
                    BusTrackerApp.onStartedStationChoosen.onStartedStationChoosen(true);

                dismiss();
            }
        });
    }


    public interface OnStartedStationChoosen {
        void  onStartedStationChoosen(boolean reverseList);
    }
}
