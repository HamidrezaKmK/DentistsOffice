package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SearchResult {

    private static SearchResult singleInstance = null;

    private ArrayList<String> firstNames = new ArrayList<>();
    private ArrayList<String> lastNames = new ArrayList<>();
    private ArrayList<Integer> patientIds = new ArrayList<>();
    private ArrayList<Integer> debt = new ArrayList<>();

    private ArrayList<Integer> unique_ids = new ArrayList<>();
    private ArrayList<Integer> sum_debts = new ArrayList<>();
    private ArrayList<String> unique_first_names = new ArrayList<>();
    private ArrayList<String> unique_last_names = new ArrayList<>();


    public static SearchResult getInstance() {
        if (singleInstance == null)
            singleInstance = new SearchResult();
        return singleInstance;
    }

    public void clear() {
        this.firstNames = null;
        this.lastNames = null;
        this.patientIds = null;
        this.debt = null;
        this.unique_last_names = null;
        this.unique_first_names = null;
        this.sum_debts = null;
        this.unique_ids = null;
    }

    public ArrayList<String> getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(ArrayList<String> firstNames) {
        this.firstNames = firstNames;
    }

    public ArrayList<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(ArrayList<String> lastNames) {
        this.lastNames = lastNames;
    }

    public ArrayList<Integer> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(ArrayList<Integer> patientIds) {
        this.patientIds = patientIds;
    }

    public ArrayList<Integer> getDebt() {
        return debt;
    }

    public void setDebt(ArrayList<Integer> debt) {
        this.debt = debt;
    }

    public void sumDebts() {
            HashMap<Integer, ArrayList<Integer>> debts = new HashMap<>();
            HashMap<Integer, String> id_name = new HashMap<>();
            HashMap<Integer, String> id_last_name = new HashMap<>();
            for (int i = 0; i < patientIds.size(); i++) {
                debts.putIfAbsent(patientIds.get(i), new ArrayList<Integer>());
                id_name.putIfAbsent(patientIds.get(i), firstNames.get(i));
                id_last_name.putIfAbsent(patientIds.get(i), lastNames.get(i));
                debts.get(patientIds.get(i)).add(debt.get(i));
            }
            for (Integer i : debts.keySet()) {
                unique_ids.add(i);
            }
            for (int i = 0; i < unique_ids.size(); i++) {
                ArrayList<Integer> a = debts.get(unique_ids.get(i));
                int sum = 0;
                for (int j = 0; j < a.size(); j++) {
                    sum += a.get(j);
                }
                sum_debts.add(sum);

                unique_first_names.add(id_name.get(unique_ids.get(i)));
                unique_last_names.add(id_last_name.get(unique_ids.get(i)));
            }
    }

    public ArrayList<Integer> getUnique_ids() {
        return unique_ids;
    }

    public void setUnique_ids(ArrayList<Integer> unique_ids) {
        this.unique_ids = unique_ids;
    }

    public ArrayList<Integer> getSum_debts() {
        return sum_debts;
    }

    public void setSum_debts(ArrayList<Integer> sum_debts) {
        this.sum_debts = sum_debts;
    }

    public ArrayList<String> getUnique_first_names() {
        return unique_first_names;
    }

    public void setUnique_first_names(ArrayList<String> unique_first_names) {
        this.unique_first_names = unique_first_names;
    }

    public ArrayList<String> getUnique_last_names() {
        return unique_last_names;
    }

    public void setUnique_last_names(ArrayList<String> unique_last_names) {
        this.unique_last_names = unique_last_names;
    }
}
