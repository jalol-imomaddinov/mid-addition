package com.mid.data.model;

import java.util.ArrayList;

/**
 * @author
 */
public class SimpleContractModelFactory {

    private static final ArrayList<SimpleContractModel> contractModels = new ArrayList<>();

    public static void makeContractModels(int size) {

	int cap = contractModels.size();

	if (size > cap) {
	    while (size >= cap) {
		cap++;
		contractModels.add(new SimpleContractModel(cap));
	    }
	}
    }

    public static ArrayList<SimpleContractModel> getContractModels() {
	return contractModels;
    }
}
