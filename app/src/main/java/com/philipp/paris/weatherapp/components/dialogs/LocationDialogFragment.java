package com.philipp.paris.weatherapp.components.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philipp.paris.weatherapp.R;
import com.philipp.paris.weatherapp.domain.Settings;
import com.philipp.paris.weatherapp.service.LocationService;
import com.philipp.paris.weatherapp.service.ServiceCallback;



public class LocationDialogFragment extends DialogFragment implements DialogInterface.OnShowListener {
    private View view;
    private AlertDialog dialog;
    private Address address;
    private TextView tvLocation;
    private ImageView imgView;
    private LocationService locationService = LocationService.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_location, null);
        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        persist();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dialog = builder.create();
        dialog.setOnShowListener(this);

        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        tvLocation = view.findViewById(R.id.tvLocation);
        imgView = view.findViewById(R.id.imageView);

        showProgressbar();
        getLocation();
    }

    private void persist() {
        Settings settings = new Settings();
        settings.setHomeLocation(address.getLatitude(), address.getLongitude());
        settings.persist();
    }

    private void showProgressbar() {
        view.findViewById(R.id.layoutLoading).setVisibility(View.VISIBLE);
        view.findViewById(R.id.layoutLocation).setVisibility(View.GONE);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    private void showLocation(Address address) {
        this.address = address;
        tvLocation.setText(address.getLocality());
        view.findViewById(R.id.layoutLoading).setVisibility(View.GONE);
        view.findViewById(R.id.layoutLocation).setVisibility(View.VISIBLE);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
    }

    private void showError() {
        tvLocation.setText(R.string.error_retrieving_location);
        imgView.setImageResource(R.drawable.ic_error_outline);
        view.findViewById(R.id.layoutLoading).setVisibility(View.GONE);
        view.findViewById(R.id.layoutLocation).setVisibility(View.VISIBLE);
    }

    private void getLocation() {
        locationService.getAddress(new ServiceCallback<Address>() {
            @Override
            public void onSuccess(Address address) {
                showLocation(address);
            }

            @Override
            public void onError(Throwable t) {
                showError();
            }
        }, new LocationService.LocationServiceCallback() {
            @Override
            public void locationDisabled() {
                dialog.cancel();
                locationService.openLocationSettings(getActivity());
            }

            @Override
            public void insufficientPermissions() {
                dialog.cancel();
                locationService.requestPermissions(getActivity());
            }
        });
    }
}
