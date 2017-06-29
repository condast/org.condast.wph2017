package org.condast.wph.ui.swt;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.condast.wph.core.definition.IModel;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class ModelTableViewer extends Composite {
	private static final long serialVersionUID = 1L;
	private TableViewer viewer;
	private Table table;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ModelTableViewer(Composite parent, int style) {
		super(parent, style);
		this.setLayout( new TableColumnLayout());
		viewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL
	            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		createColumns(viewer);
		viewer.setContentProvider(new ArrayContentProvider());

		table = viewer.getTable();
		table.setBounds(54, 141, 85, 45);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		  // define layout for the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
  
	}

	private void createColumns(TableViewer viewer2) {
		TableViewerColumn colFirstName = new TableViewerColumn(viewer, SWT.NONE);
		colFirstName.getColumn().setWidth(200);
		colFirstName.getColumn().setText("ID");
		colFirstName.setLabelProvider(new ColumnLabelProvider() {
			private static final long serialVersionUID = 1L;

			@Override
		    public String getText(Object element) {
		        IModel model = (IModel) element;
		    	return model.getId();
		    }
		});	
	}

	public void setInput( IModel[] models ){
        viewer.setInput( models );
	
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
