package com.example.veryhomepage.recipe_liveStream;
//123456

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.veryhomepage.R;
import com.example.veryhomepage.adapters.VerticalRecyclerViewAdapter;
import com.example.veryhomepage.adapters.models.HorizontalModel;
import com.example.veryhomepage.adapters.models.VerticalModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {
    private androidx.recyclerview.widget.RecyclerView verticalRecyclerView;
    private ArrayList<VerticalModel> arrayListVertical;
    private VerticalRecyclerViewAdapter adapter;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrayListVertical = new ArrayList<>();
        verticalRecyclerView = getActivity().findViewById(R.id.recyclerView);
        verticalRecyclerView.setHasFixedSize(true);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));

        adapter = new VerticalRecyclerViewAdapter(getActivity(),arrayListVertical);

        verticalRecyclerView.setAdapter(adapter);

        setData();
    }

    public void setData(){
        for(int i=1;i<=10;i++) {
            VerticalModel mVerticalModel = new VerticalModel();
            mVerticalModel.setTitle("Title: " + i);
            ArrayList<HorizontalModel> arrayList = new ArrayList<>();

            for(int j=0;j<=10;j++) {
                HorizontalModel mHorizontalModel = new HorizontalModel();
                mHorizontalModel.setDescription("Description: "+j);
                mHorizontalModel.setName("Name: "+j);

                arrayList.add(mHorizontalModel);
            }
            mVerticalModel.setArrayList(arrayList);

            arrayListVertical.add(mVerticalModel);
        }
        adapter.notifyDataSetChanged();
    }
}


