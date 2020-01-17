package www.gianlucaveschi.mijirecipesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sheck on 25/09/2019.
 */

public class Stove implements Parcelable {
    private int imageResource; //int because R.layout.image is internally saved as an int
    private String title;
    private String description;

    public Stove(int imageResource, String title, String description) {
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int mImageResource) {
        this.imageResource = mImageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Parcelable methods
     * */
    protected Stove(Parcel in) {
        imageResource = in.readInt();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<Stove> CREATOR = new Creator<Stove>() {
        @Override
        public Stove createFromParcel(Parcel in) {
            return new Stove(in);
        }

        @Override
        public Stove[] newArray(int size) {
            return new Stove[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageResource);
        dest.writeString(title);
        dest.writeString(description);
    }
}


