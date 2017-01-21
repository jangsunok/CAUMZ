package jdmz.jdmz;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by jang on 2017. 1. 3..
 */
public class PropertyManager {


    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getMyContext());
        mEditor = mPrefs.edit();
    }

    private static final String LIKED = "liked";



    public void setLiked(ArrayList<Integer> likeArray) {
        String str = "";
        for(int i=0;i<likeArray.size();i++){
            str+=" "+likeArray.get(i);
        }
        mEditor.putString(LIKED, str);
        mEditor.commit();
    }

    public void addLiked(int com_id) {
        mEditor.putString(LIKED, mPrefs.getString(LIKED,"")+" "+com_id);
        mEditor.commit();
    }

    public void deleteLiked(int com_id){
        ArrayList<Integer> likeArray = getLiked();

        likeArray.remove((Integer)com_id);
        setLiked(likeArray);
    }

    public ArrayList<Integer> getLiked() {
        String str = mPrefs.getString(LIKED,"");
        String[] liked = str.split(" ");
        ArrayList<Integer> ret = new ArrayList<>();
        for(int i=1;i<liked.length;i++){
            if(liked[i]!="" && liked[i].length()>0){
                ret.add(Integer.parseInt(liked[i]));
            }
        }
        return ret;
    }



}
