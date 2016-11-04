package edu.temple.webbrowser;

import android.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ben on 11/3/2016.
* Class used to navigate through a list of WebBrowserFragment tabs
 * Left as a list of Fragments for reuse
 */

public class FragmentArrayList {
    private ArrayList<Fragment> fragments;
    private int iterator;
    private boolean isEmpty;

    public FragmentArrayList (){
        fragments = new ArrayList<>();
        iterator = 0;
        isEmpty = true;
    }

    public void add(Fragment fragment){
        fragments.add(fragment);
        if(isEmpty){
            isEmpty = false;
        } else {
            iterator++;
        }
    }

    public Fragment get(){
        return fragments.get(iterator);
    }

    public Fragment getNext(){
        if(hasNext()){
            iterator++;
            return fragments.get(iterator);
        } else {
            return null;
        }
    }

    public Fragment getPrevious(){
        if(hasPrevious()){
            iterator --;
            return fragments.get(iterator);
        } else {
            return null;
        }
    }

    public Fragment removeCurrent(){
        Log.d("Iterator: ", String.valueOf(iterator));
        if(hasPrevious()){
            fragments.remove(iterator);
            return getPrevious();
        } else if(hasNext()){
            fragments.remove(iterator);
            return fragments.get(iterator);
        } else {
            fragments.remove(iterator);
            isEmpty = true;
            return null;
        }
    }

    private boolean hasNext(){
        try{
            fragments.get(iterator+1);
            return true;
        } catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    private boolean hasPrevious(){
        try{
            fragments.get(iterator-1);
            return true;
        } catch (IndexOutOfBoundsException e){
            return false;
        }
    }
}
