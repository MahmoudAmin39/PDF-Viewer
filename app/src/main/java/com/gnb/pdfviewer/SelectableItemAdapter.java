package com.gnb.pdfviewer;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectableItemAdapter extends RecyclerView.Adapter<SelectableItemAdapter.SelectableItemViewHolder> implements ItemSelectionHandlerByIndex {

    private List<SelectableItem> selectableItems;
    private ItemSelectionHandlerByName selectionHandler;

    public SelectableItemAdapter(List<SelectableItem> selectableItems, ItemSelectionHandlerByName selectionHandler) {
        this.selectableItems = selectableItems;
        this.selectionHandler = selectionHandler;
    }

    @NonNull
    @Override
    public SelectableItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selectable_section, parent, false);
        return new SelectableItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectableItemViewHolder holder, int position) {
        holder.bindSelectableItem(selectableItems.get(position));
    }

    @Override
    public int getItemCount() {
        return selectableItems.size();
    }

    @Override
    public void onItemSelected(int position, boolean shouldStartAnimation) {
        for (int index = 0; index < selectableItems.size(); index++) {
            if (index == position) {
                selectableItems.get(index).setSelected(true);
            } else {
                selectableItems.get(index).setSelected(false);
            }
        }

        notifyDataSetChanged();
        selectionHandler.onItemSelected(selectableItems.get(position).getItemName(), shouldStartAnimation);
    }

    static class SelectableItemViewHolder extends RecyclerView.ViewHolder {

        private Button selectableButton;

        SelectableItemViewHolder(@NonNull View itemView, final ItemSelectionHandlerByIndex selectionHandler) {
            super(itemView);
            selectableButton = itemView.findViewById(R.id.button_selectable);
            selectableButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (selectionHandler != null) {
                                selectionHandler.onItemSelected(getAdapterPosition(), true);
                            }
                        }
                    }
            );
        }

        void bindSelectableItem(SelectableItem selectableItem) {
            selectableButton.setText(selectableItem.getItemName());
            if (selectableItem.isSelected()) {
                selectableButton.setBackground(getDrawable(R.drawable.rect_rounded_filled));
                selectableButton.setTextColor(getColor(android.R.color.white));
            } else {
                selectableButton.setBackground(getDrawable(R.drawable.rect_rounded));
                selectableButton.setTextColor(getColor(R.color.colorAccent));
            }
        }

        private Drawable getDrawable(int drawableResource) {
            return ContextCompat.getDrawable(itemView.getContext(), drawableResource);
        }

        private int getColor(int colorResource) {
            return ContextCompat.getColor(itemView.getContext(), colorResource);
        }
    }
}
