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
            for (int i = 0; i < patientIds.size(); i++) {
                debts.putIfAbsent(patientIds.get(i), new ArrayList<Integer>());
                debts.get(patientIds.get(i)).add(debt.get(i));
            }
            for (Integer i : debts.keySet()) {
                unique_ids.add(i);
            }
            for (int i = 0; i < unique_ids.size(); i++) {
                ArrayList<Integer> a = debts.get(unique_ids.get(i));
                int sum = 0;
                for (int j = 0; j < a.size(); j++) {
                    sum += a.get(i);
                }
                sum_debts.add(sum);
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
}
