package tests.java.com;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FileParser {

	Map<String, Map<String, String>> galPredictionResults;
	Map<String, Map<String, String>> galWorkersResults;
	Map<String, Map<String, String>> galcPredictionResults;
	Map<String, Map<String, String>> galcWorkersResults;

	public FileParser() {
		this.galPredictionResults = new HashMap<String, Map<String, String>>();
		this.galWorkersResults = new HashMap<String, Map<String, String>>();
		this.galcPredictionResults = new HashMap<String, Map<String, String>>();
		this.galcWorkersResults = new HashMap<String, Map<String, String>>();
	}

	public Map<String, Map<String, String>> ParseGALPredictionObjectsFile(String filePath) {
		FileInputStream fstream = null;

		try {
			fstream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		int lineCount = 1;
		try {
			while ((line = br.readLine()) != null) {
				if (lineCount > 1) {
					String[] entries = line.split("\t");
					String objectName = entries[0];
					Map<String, String> estimatedObjectValues = new HashMap<String, String>();
					estimatedObjectValues.put("DS MaxLikelihood", entries[1]);
					estimatedObjectValues.put("DS MinCost", entries[2]);
					estimatedObjectValues.put("MV MaxLikelihood", entries[3]);
					estimatedObjectValues.put("MV MinCost", entries[4]);
					galPredictionResults.put(objectName, estimatedObjectValues);
				} 
				else {
					lineCount++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return galPredictionResults;
	}
	
	public Map<String, Map<String, String>> ParseGALCPredictionObjectsFile(String filePath) {
		FileInputStream fstream = null;

		try {
			fstream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		int lineCount = 1;
		try {
			while ((line = br.readLine()) != null) {
				if (lineCount > 1) {
					String[] entries = line.split("\t");
					String objectName = entries[0];
					Map<String, String> estimatedObjectValues = new HashMap<String, String>();
					estimatedObjectValues.put("est_value", entries[1]);
					estimatedObjectValues.put("est_zeta", entries[2]);
					galcPredictionResults.put(objectName, estimatedObjectValues);
				} 
				else {
					lineCount++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return galcPredictionResults;
	}


	public Map<String, Map<String, String>> ParseGALWorkersResultsFile(String filePath) {
		FileInputStream fstream = null;

		try {
			fstream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		int lineCount = 1;
		try {
			while ((line = br.readLine()) != null) {
				if (lineCount > 1) {
					String[] entries = line.split("\t");
					String workerName = entries[0];
					Map<String, String> estimatedWorkersQuality = new HashMap<String, String>();
					estimatedWorkersQuality.put("MaxLikelihood", String.format("%.2f",  Double.parseDouble(entries[1])));
					estimatedWorkersQuality.put("MinCost", String.format("%.2f", Double.parseDouble(entries[2])));
					estimatedWorkersQuality.put("ExpectedCost", String.format("%.2f", Double.parseDouble(entries[3])));
					galWorkersResults.put(workerName, estimatedWorkersQuality);
				} 
				else {
					lineCount++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return galWorkersResults;
	}
	
	
	public Map<String, Map<String, String>> ParseGALCWorkersResultsFile(String filePath) {
		FileInputStream fstream = null;

		try {
			fstream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		int lineCount = 1;
		try {
			while ((line = br.readLine()) != null) {
				if (lineCount > 1) {
					String[] entries = line.split("\t");
					String workerName = entries[0];
					Map<String, String> estimatedWorkersQuality = new HashMap<String, String>();
					estimatedWorkersQuality.put("est_mu", entries[1]);
					estimatedWorkersQuality.put("est_sigma", entries[2]);
					estimatedWorkersQuality.put("est_rho", entries[3]);
					galcWorkersResults.put(workerName, estimatedWorkersQuality);
				} 
				else {
					lineCount++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return galcWorkersResults;
	}


}
