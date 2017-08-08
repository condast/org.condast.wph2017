package org.condast.wph.ui.swt;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import java.text.SimpleDateFormat;

import org.condast.commons.strings.StringStyler;
import org.condast.wph.core.definition.IContainer;
import org.condast.wph.core.definition.IJourney;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

public class JourneyTableViewer extends Composite {
	private static final long serialVersionUID = 1L;
	
	private enum Columns{
		ID,
		STARTED,
		ETA,
		PARTY,
		LONGITUDE,
		LATITUDE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
		
		public int weight(){
			int weight = 20;
			switch( this ){
			case ID:
				//weight = 50;
				break;
			default:
				break;
			}
			return weight;
		}
	}
	
	private TableViewer viewer;
	private Table table;
	private TableColumnLayout tableColumnLayout;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public JourneyTableViewer(Composite parent, int style) {
		super(parent, style);
		tableColumnLayout = new TableColumnLayout();
		this.setLayout( tableColumnLayout);
		viewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL
	            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		for( Columns column: Columns.values())
			createColumns( column, viewer);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider( new JourneyLabelProvider());
		table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
  
	}

	private void createColumns( Columns column, TableViewer viewer2) {
		TableViewerColumn colFirstName = new TableViewerColumn(viewer, SWT.NONE);
		colFirstName.getColumn().setText( column.toString());
		tableColumnLayout.
	    setColumnData(colFirstName.getColumn(), new ColumnWeightData( column.weight()));
	}

	public void setInput( IJourney[] journey ){
        viewer.setInput(  journey );
	}

	protected class JourneyLabelProvider extends LabelProvider implements ITableLabelProvider{
		private static final long serialVersionUID = 1L;


		@Override
		public String getColumnText(Object element, int columnIndex) {
			Columns column = Columns.values()[columnIndex];
			String text = super.getText(element);
			IJourney journey = (IJourney) element;
		    IContainer container = journey.getContainer();
			String pattern = "MM-dd-yyyy";
		    SimpleDateFormat format = new SimpleDateFormat(pattern);
		 	switch( column ){
			case ID:
				text = container.getTag();
				break;
			case PARTY:
				//text = 
				break;
			case STARTED:
				text = format.format( container.getDeparture());
				break;
			case ETA:
				text = format.format( container.getETA());
				break;
			case LONGITUDE:
				text = String.valueOf( container.getLngLat().getLongitude());
				break;
			case LATITUDE:
				text = String.valueOf( container.getLngLat().getLatitude());
				break;
			default:
				break;
			}
			return text;
		}

		@Override
		public Image getColumnImage(Object arg0, int columnIndex) {
			Image image = null;
			return image;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}	
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
