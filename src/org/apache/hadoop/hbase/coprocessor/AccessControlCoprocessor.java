package org.apache.hadoop.hbase.coprocessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.util.Bytes;

public class AccessControlCoprocessor extends BaseEndpointCoprocessor {
	// @Override
	public Get preGet(CoprocessorEnvironment e, Get get)
			throws CoprocessorException {

		// check permissions..
		/*
		 * if (access_not_allowed) { throw new
		 * AccessDeniedException("User is not allowed to access."); }
		 */
		return get;
	}

	// override prePut(), preDelete(), etc.
}

// Aggregation implementation at a region.
class ColumnAggregationEndpoint extends BaseEndpointCoprocessor {
	// @Override
	// Scan the region by the given family and qualifier. Return the aggregation
	// result.
	public int sum(byte[] family, byte[] qualifier) throws IOException {
		// aggregate at each region
		Scan scan = new Scan();
		scan.addColumn(family, qualifier);
		int sumResult = 0;
		// use an internal scanner to perform scanning.
		InternalScanner scanner = null;// getEnvironment().getRegion().getScanner(scan);
		try {
			List<KeyValue> curVals = new ArrayList<KeyValue>();
			boolean done = false;
			do {
				curVals.clear();
				done = scanner.next(curVals);
				KeyValue kv = curVals.get(0);
				sumResult += Bytes.toInt(kv.getValue());
			} while (done);
		} finally {
			scanner.close();
		}
		return sumResult;
	}
}
