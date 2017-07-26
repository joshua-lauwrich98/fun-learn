package anchovy.net.funlearn.other;

import android.content.Context;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import anchovy.net.funlearn.R;

/**
 * Created by DarKnight98 on 7/25/2017.
 */

public class MateriExpendableListDataSource {

    public static Map<String, List<String>> getData (Context context, int jenjang, int pelajaran) {
        Map<String, List<String>> expandableListData = new TreeMap<>();

        if (jenjang == 0) {
            List<String> listKelas = Arrays.asList(context.getResources().getStringArray(R.array.sd_class_expandable_drawer));
            
            if (pelajaran == 0) {
                List<String> kelas1 = Arrays.asList(context.getResources().getStringArray(R.array.kelas1_mat_item_array));
                List<String> kelas2 = Arrays.asList(context.getResources().getStringArray(R.array.kelas2_mat_item_array));
                List<String> kelas3 = Arrays.asList(context.getResources().getStringArray(R.array.kelas3_mat_item_array));
                List<String> kelas4 = Arrays.asList(context.getResources().getStringArray(R.array.kelas4_mat_item_array));
                List<String> kelas5 = Arrays.asList(context.getResources().getStringArray(R.array.kelas5_mat_item_array));
                List<String> kelas6 = Arrays.asList(context.getResources().getStringArray(R.array.kelas6_mat_item_array));

                expandableListData.put(listKelas.get(0), kelas1);
                expandableListData.put(listKelas.get(1), kelas2);
                expandableListData.put(listKelas.get(2), kelas3);
                expandableListData.put(listKelas.get(3), kelas4);
                expandableListData.put(listKelas.get(4), kelas5);
                expandableListData.put(listKelas.get(5), kelas6);

            } else if (pelajaran == 1) {
                List<String> kelas1 = Arrays.asList(context.getResources().getStringArray(R.array.kelas1_bi_item_array));
                List<String> kelas2 = Arrays.asList(context.getResources().getStringArray(R.array.kelas2_bi_item_array));
                List<String> kelas3 = Arrays.asList(context.getResources().getStringArray(R.array.kelas3_bi_item_array));
                List<String> kelas4 = Arrays.asList(context.getResources().getStringArray(R.array.kelas4_bi_item_array));
                List<String> kelas5 = Arrays.asList(context.getResources().getStringArray(R.array.kelas5_bi_item_array));
                List<String> kelas6 = Arrays.asList(context.getResources().getStringArray(R.array.kelas6_bi_item_array));

                expandableListData.put(listKelas.get(0), kelas1);
                expandableListData.put(listKelas.get(1), kelas2);
                expandableListData.put(listKelas.get(2), kelas3);
                expandableListData.put(listKelas.get(3), kelas4);
                expandableListData.put(listKelas.get(4), kelas5);
                expandableListData.put(listKelas.get(5), kelas6);

            } else if (pelajaran == 2) {
                List<String> kelas1 = Arrays.asList(context.getResources().getStringArray(R.array.kelas1_bing_item_array));
                List<String> kelas2 = Arrays.asList(context.getResources().getStringArray(R.array.kelas2_bing_item_array));
                List<String> kelas3 = Arrays.asList(context.getResources().getStringArray(R.array.kelas3_bing_item_array));
                List<String> kelas4 = Arrays.asList(context.getResources().getStringArray(R.array.kelas4_bing_item_array));
                List<String> kelas5 = Arrays.asList(context.getResources().getStringArray(R.array.kelas5_bing_item_array));
                List<String> kelas6 = Arrays.asList(context.getResources().getStringArray(R.array.kelas6_bing_item_array));

                expandableListData.put(listKelas.get(0), kelas1);
                expandableListData.put(listKelas.get(1), kelas2);
                expandableListData.put(listKelas.get(2), kelas3);
                expandableListData.put(listKelas.get(3), kelas4);
                expandableListData.put(listKelas.get(4), kelas5);
                expandableListData.put(listKelas.get(5), kelas6);

            } else if (pelajaran == 3) {

            }
        }

        return expandableListData;
    }
}
