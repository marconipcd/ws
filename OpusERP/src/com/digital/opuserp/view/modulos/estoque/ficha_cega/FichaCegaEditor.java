package com.digital.opuserp.view.modulos.estoque.ficha_cega;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class FichaCegaEditor extends Window {

	VerticalLayout vl_root;
	public FichaCegaEditor(){
		
		VerticalLayout vl_root = new VerticalLayout();
		setContent(vl_root);
		
		buildLayout();
	}
	
	private void buildLayout(){
		
		vl_root.addComponent(new VerticalLayout(){
			{
				addComponent(new Label("Ficha cega "));
			}
		});
		
	}
}
