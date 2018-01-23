package com.example.krug.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    ArrayAdapter<MainActivity.Horaire> listAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    // TODO: getActivityRename and change types and number of parameters
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);


        ListView list = (ListView) v.findViewById(R.id.ListView);

    listAdapter  = new ListArrayAdapter(getActivity(), R.layout.fragment_list);

        list.setAdapter(listAdapter);

    return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ListArrayAdapter extends ArrayAdapter<MainActivity.Horaire> {


        public ListArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                v = getLayoutInflater().inflate(R.layout.line, null);
            }

            MainActivity.Horaire h = this.getItem(position);
            ((TextView) v.findViewById(R.id.heureDView)).setText(h.HeuresD);
            ((TextView) v.findViewById(R.id.heureFView)).setText(h.HeuresF);
            ((TextView) v.findViewById(R.id.latView)).setText(h.Latitude);
            ((TextView) v.findViewById(R.id.longView)).setText(h.Longitude);
            ((TextView) v.findViewById(R.id.lieuView)).setText(h.Lieu);
            ((TextView) v.findViewById(R.id.margeView)).setText(h.Marge);
            ((TextView) v.findViewById(R.id.coursView)).setText(h.Cours);

            return v;

        }

    }
    void setData(final List<MainActivity.Horaire> tableau) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listAdapter.addAll(tableau);
                listAdapter.notifyDataSetChanged();
            }
        });

    }
}
