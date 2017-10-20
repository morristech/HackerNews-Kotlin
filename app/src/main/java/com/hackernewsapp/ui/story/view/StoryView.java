package com.hackernewsapp.ui.story.view;

import com.hackernewsapp.data.model.Story;

import java.util.ArrayList;

/**
 * Created by tosin on 3/13/2017.
 */

public interface StoryView {

    void init();

    void populateRecyclerView();

    void pullToRefresh();

    void refresh(String topStories, boolean refresh);

    void implementScrollListener();

    void setLayoutVisibility();

    void displayOfflineSnackbar();

    void hideOfflineSnackBar();

    void doAfterFetchStory();

    void setAdapter(Integer storLoaded, ArrayList<Story> listArrayList,
                    ArrayList<Story> refreshedArrayList, boolean loadmore, Integer totalNum);

}
