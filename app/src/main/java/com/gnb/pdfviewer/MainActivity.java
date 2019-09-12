package com.gnb.pdfviewer;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener, ItemSelectionHandlerByName {

    private RecyclerView recyclerView;
    private PDFView pdfView;
    private AnimationSet invisibilityAnimationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfView = findViewById(R.id.pdfView);
        recyclerView = findViewById(R.id.view);

        // Starting animation logic
        int ANIMATION_DURATION = 400;
        // Visibility Animation
        Animation scaleInAnimation = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,1);
        scaleInAnimation.setDuration(ANIMATION_DURATION);
        Animation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setDuration(ANIMATION_DURATION);
        final AnimationSet visibilityAnimationSet = new AnimationSet(false);
        visibilityAnimationSet.addAnimation(scaleInAnimation);
        visibilityAnimationSet.addAnimation(fadeInAnimation);
        // Invisibility Animation
        Animation scaleOutAnimation = new ScaleAnimation(1, 1, 1, 0, Animation.RELATIVE_TO_SELF,0, Animation.RELATIVE_TO_SELF,1);
        scaleOutAnimation.setDuration(ANIMATION_DURATION);
        Animation fadeOutAnimation = new AlphaAnimation(1, 0);
        fadeOutAnimation.setDuration(ANIMATION_DURATION);
        invisibilityAnimationSet = new AnimationSet(false);
        invisibilityAnimationSet.addAnimation(scaleOutAnimation);
        invisibilityAnimationSet.addAnimation(fadeOutAnimation);
        scaleInAnimation.setAnimationListener(this);
        scaleOutAnimation.setAnimationListener(this);

        // PDF recyclerView logic
        pdfView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recyclerView.getVisibility() == View.GONE) {
                            recyclerView.startAnimation(visibilityAnimationSet);
                        } else if (recyclerView.getVisibility() == View.VISIBLE) {
                            recyclerView.startAnimation(invisibilityAnimationSet);
                        }
                    }
                }
        );

        // RecyclerView logic
        String[] assets = {"drylab.pdf", "example.pdf", "power_company_profile.pdf", "prince_catalogue.pdf", "sample.pdf"};
        SelectableItemAdapter adapter = new SelectableItemAdapter(getItemsFromAssets(assets), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        ((ItemSelectionHandlerByIndex) adapter).onItemSelected(0, false);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.startAnimation(invisibilityAnimationSet);
            }
        }, 2000);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
        } else if (recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onItemSelected(String itemName, boolean shouldStartAnimation) {
        loadPdfFromAsset(itemName);
        if (shouldStartAnimation) {
            recyclerView.startAnimation(invisibilityAnimationSet);
        }
    }

    private void loadPdfFromAsset(String assetName) {
        pdfView.fromAsset(assetName)
                .swipeHorizontal(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .load();
    }

    private List<SelectableItem> getItemsFromAssets(String[] assets) {
        List<SelectableItem> selectableItems = new ArrayList<>();
        for (String asset : assets) {
            SelectableItem item = new SelectableItem();
            item.setSelected(false);
            item.setItemName(asset);
            selectableItems.add(item);
        }
        return selectableItems;
    }
}
