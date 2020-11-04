package istic.projet.estampille;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new HomeFragment(); //ChildFragment at position 0
            case 1:
                return new WritePackagingNumberFragment(); //ChildFragment1 at position 1
            case 2:
                return new HistoryFragment(); //ChildFragment2 at position 2
            case 3:
                return new LookAroundFragment(); //ChildFragment3 at position 3
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 4; //three fragments
    }
}
