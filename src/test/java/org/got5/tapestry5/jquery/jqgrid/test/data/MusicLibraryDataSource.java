package org.got5.tapestry5.jquery.jqgrid.test.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.tapestry5.PropertyConduit;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.got5.tapestry5.jquery.jqgrid.test.services.MusicLibrary;

public class MusicLibraryDataSource implements GridDataSource {

	private List<Track> tracks;

	public MusicLibraryDataSource(MusicLibrary musicLib) {

		this.tracks = musicLib.getTracks();

	}

	public int getAvailableRows() {
		return tracks.size();
	}

	@SuppressWarnings("unchecked")
	public Class getRowType() {
		return null;
	}

	public Object getRowValue(int index) {
		return tracks.get(index);
	}

	
	@SuppressWarnings("unchecked")
	public void prepare(int startIndex, int endIndex,
			List<SortConstraint> sortConstraints) {

		/*for (SortConstraint constraint : sortConstraints) {
			final ColumnSort sort = constraint.getColumnSort();

			if (sort == ColumnSort.UNSORTED) {
				continue;
			}

			final PropertyConduit conduit = constraint.getPropertyModel()
					.getConduit();

			final Comparator valueComparator = new Comparator<Comparable>() {
				public int compare(Comparable o1, Comparable o2) {
					// Simplify comparison, and handle case where both are
					// nulls.
					if (o1.equals(o2)) {
						return 0;
					} else if (o2 == null) {
						return 1;
					} else if (o1 == null) {
						return -1;
					}
					return o1.compareTo(o2);
				}
			};

			final Comparator rowComparator = new Comparator() {
				public int compare(Object row1, Object row2) {
					Comparable value1 = (Comparable) conduit.get(row1);
					Comparable value2 = (Comparable) conduit.get(row2);

					return valueComparator.compare(value1, value2);
				}
			};

			final Comparator reverseComparator = new Comparator() {
				public int compare(Object o1, Object o2) {
					int modifier = sort == ColumnSort.ASCENDING ? 1 : -1;

					return modifier * rowComparator.compare(o1, o2);
				}
			};

			// We can freely sort this list because its just a copy.
			Collections.sort(celebrities, reverseComparator);

		}*/
	}

}
