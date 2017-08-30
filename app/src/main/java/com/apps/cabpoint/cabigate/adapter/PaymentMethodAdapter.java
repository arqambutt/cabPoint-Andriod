package com.apps.cabpoint.cabigate.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apps.cabpoint.cabigate.R;
import com.apps.cabpoint.cabigate.models.PaymentMethod;
import com.apps.cabpoint.cabigate.restapi.ApiCalls;
import com.apps.cabpoint.cabigate.restapi.PaxApiCall;
import com.apps.cabpoint.cabigate.restapi.PaxApiCallListner;
import com.apps.cabpoint.cabigate.utils.StoragePreference;
import com.apps.cabpoint.cabigate.views.MobiTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Abdul Ghani on 8/2/2017.
 */

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.PaymentViewHolder> {

    private List<PaymentMethod> paymentMethods;
    private Context context;

    public PaymentMethodAdapter(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = null;
        this.paymentMethods = paymentMethods;
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {

        ImageView paymentImage;
        MobiTextView paymentType, paymentDefault, deletePayment;
        LinearLayout item;

        public PaymentViewHolder(View itemView) {
            super(itemView);
            paymentImage = (ImageView) itemView.findViewById(R.id.payment_method_image);
            paymentType = (MobiTextView) itemView.findViewById(R.id.payment_type_text);
            paymentDefault = (MobiTextView) itemView.findViewById(R.id.selected_payment);
            deletePayment = (MobiTextView) itemView.findViewById(R.id.delete_payment);
            item = (LinearLayout) itemView.findViewById(R.id.list_item);
        }
    }

    @Override
    public PaymentMethodAdapter.PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_list_view, parent, false);
        context = v.getContext();
        return new PaymentMethodAdapter.PaymentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PaymentMethodAdapter.PaymentViewHolder holder, final int position) {
        holder.paymentImage.setImageResource(getImageResource(paymentMethods.get(position).getPaymentImage()));
        holder.paymentType.setText(paymentMethods.get(position).getPaymentName());
        if (paymentMethods.get(position).isDefault()) {
            holder.paymentDefault.setVisibility(View.VISIBLE);
        } else {
            holder.paymentDefault.setVisibility(View.INVISIBLE);
        }

        if (paymentMethods.get(position).getPaymentImage() != PaymentMethod.cash) {
            holder.deletePayment.setVisibility(View.VISIBLE);
        } else {
            holder.deletePayment.setVisibility(View.GONE);
        }
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                Context context = holder.itemView.getContext();
                if (paymentMethods.get(position).getPaymentImage() == PaymentMethod.cash) {
                    StoragePreference.setPaymentMethod(context, PaxApiCall.cash);
                    changeDefaultCard(position);
                    notifyDataSetChanged();
                } else if (paymentMethods.get(position).getPaymentImage() == PaymentMethod.paypal) {
                    StoragePreference.setPaymentMethod(context, PaxApiCall.paypal);
                    String paypalId = paymentMethods.get(position).getId();
                    setDefaultPayPal(paypalId, holder);
                } else if (paymentMethods.get(position).getPaymentImage() == PaymentMethod.credit) {
                    StoragePreference.setPaymentMethod(context, PaxApiCall.credit);
                    String cardId = paymentMethods.get(position).getId();
                    setDefaultCard(cardId, holder);
                }
                return true;
            }
        });
        holder.deletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningDialog(holder, paymentMethods.get(holder.getAdapterPosition()).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentMethods.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private int getImageResource(int paymentImage) {
        if (paymentImage == 0) {
            return R.drawable.ic_cash_color;
        } else if (paymentImage == 1) {
            return R.drawable.ic_credit_color;
        } else {
            return R.drawable.ic_paypal_color;
        }
    }

    private void changeDefaultCard(int position) {
        for (PaymentMethod paymentMethod : paymentMethods) {
            paymentMethod.setDefault(false);
        }
        paymentMethods.get(position).setDefault(true);
    }

    private void setDefaultCard(String cardId, final PaymentMethodAdapter.PaymentViewHolder holder) {
        String token = StoragePreference.getTOKEN(holder.itemView.getContext());
        String userId = StoragePreference.getUserId(holder.itemView.getContext());
        ApiCalls.setDefult(userId, cardId, token, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                defaultCardResponse(jsonObject, holder);
            }
        });
    }

    private void defaultCardResponse(JSONObject jsonObject, PaymentMethodAdapter.PaymentViewHolder holder) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                changeDefaultCard(holder.getAdapterPosition());
                notifyDataSetChanged();
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultPayPal(String payPalId, final PaymentMethodAdapter.PaymentViewHolder holder) {
        String userId = StoragePreference.getUserId(holder.itemView.getContext());
        ApiCalls.setDefaultPaypal(userId, payPalId, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                defaultPaypalResponse(jsonObject, holder);
            }
        });
    }

    private void defaultPaypalResponse(JSONObject jsonObject, PaymentMethodAdapter.PaymentViewHolder holder) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                changeDefaultCard(holder.getAdapterPosition());
                notifyDataSetChanged();
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deleteCreditCard(String cardId, PaymentViewHolder holder) {
        String token = StoragePreference.getTOKEN(holder.itemView.getContext());
        String userId = StoragePreference.getUserId(holder.itemView.getContext());
        final int position = holder.getAdapterPosition();
        ApiCalls.deleteCard(userId, cardId, token, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                deleteCardResponse(jsonObject, position);
            }
        });
    }

    private void deleteCardResponse(JSONObject jsonObject, int position) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                if(isDefaultCardAndPaymentMethod(position)){
                    paymentMethods.remove(position);
                    notifyItemRemoved(position);
                    setDefaultToCash();
                }else {
                    paymentMethods.remove(position);
                    notifyItemRemoved(position);
                }
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deletePayPal(String payPalId, final PaymentMethodAdapter.PaymentViewHolder holder) {
        String userId = StoragePreference.getUserId(holder.itemView.getContext());
        final int position = holder.getAdapterPosition();
        ApiCalls.deletePaypal(userId, payPalId, new PaxApiCallListner() {
            @Override
            public void onApiRequestResult(JSONObject jsonObject) {
                deletePaypalResponse(jsonObject, position);
            }
        });
    }

    private void deletePaypalResponse(JSONObject jsonObject, int position) {
        try {
            int status = jsonObject.getInt(PaxApiCall.status);
            if (status == 1) {
                if(isDefaultCardAndPaymentMethod(position)){
                    paymentMethods.remove(position);
                    notifyItemRemoved(position);
                    setDefaultToCash();
                }else {
                    paymentMethods.remove(position);
                    notifyItemRemoved(position);
                }
            } else {
                String msg = jsonObject.getString(PaxApiCall.errorMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void warningDialog(final PaymentViewHolder holder, final String cardId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
        alert.setTitle("Do you want delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int positon = holder.getAdapterPosition();
                if (paymentMethods.get(positon).getPaymentImage() == PaymentMethod.credit) {
                    deleteCreditCard(cardId, holder);
                } else if (paymentMethods.get(positon).getPaymentImage() == PaymentMethod.paypal) {
                    deletePayPal(cardId, holder);
                }
            }
        });

        alert.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    private boolean isDefaultCardAndPaymentMethod(int positon) {
        String paymentMethod = StoragePreference.getPaymentMethod(context);
        if (paymentMethods.get(positon).isDefault()) {
            if (paymentMethods.get(positon).getPaymentImage() == PaymentMethod.credit) {

                return paymentMethod.equals(PaxApiCall.credit);

            } else if (paymentMethods.get(positon).getPaymentImage() == PaymentMethod.cash) {

                return paymentMethod.equals(PaxApiCall.cash);

            } else if (paymentMethods.get(positon).getPaymentImage() == PaymentMethod.paypal) {

                return paymentMethod.equals(PaxApiCall.paypal);

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    void setDefaultToCash(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StoragePreference.setPaymentMethod(context, PaxApiCall.cash);
                changeDefaultCard(0);
                notifyDataSetChanged();
            }
        },500);
    }
}
