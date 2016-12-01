package org.hisp.dhis.client.sdk.ui.rows;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hisp.dhis.client.sdk.ui.R;
import org.hisp.dhis.client.sdk.ui.models.FormEntity;
import org.hisp.dhis.client.sdk.ui.models.FormEntityText;
import org.hisp.dhis.client.sdk.ui.utils.FormUtils;

class TextRowView implements RowView {

    TextRowView() {
        // explicit empty constructor
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new TextViewHolder(inflater.inflate(
                R.layout.recyclerview_row_textview, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, FormEntity formEntity) {
        FormEntityText formEntityText = ((FormEntityText) formEntity);
        ((TextViewHolder) viewHolder).update(formEntityText);
    }

    private static class TextViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewLabel;
        final TextView textViewValue;

        TextViewHolder(View itemView) {
            super(itemView);

            textViewLabel = (TextView) itemView.findViewById(R.id.textview_row_label);
            textViewValue = (TextView) itemView.findViewById(R.id.textview_row_textview);
        }

        public void update(FormEntityText entityText) {

            textViewLabel.setText(FormUtils.getFormEntityLabel(entityText));
            textViewValue.setText(entityText.getValue());

            if (entityText.isLocked()) {
                textViewValue.setEnabled(false);
                textViewValue.setClickable(false);
            } else {
                textViewValue.setEnabled(true);
                textViewValue.setClickable(true);
            }
        }
    }
}
