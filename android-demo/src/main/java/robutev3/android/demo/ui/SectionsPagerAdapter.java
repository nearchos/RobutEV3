package robutev3.android.demo.ui;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import robutev3.android.demo.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = { R.string.Brick_info, R.string.Sound, R.string.Motors, R.string.Tank, R.string.Ultrasonic };

    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return BrickInfoFragment.newInstance();
            case 1: return SoundFragment.newInstance();
            case 2: return MotorsFragment.newInstance();
            case 3: return TankFragment.newInstance();
            case 4: return UltrasonicFragment.newInstance();
            default: throw new RuntimeException("Invalid tab position: " + position);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}