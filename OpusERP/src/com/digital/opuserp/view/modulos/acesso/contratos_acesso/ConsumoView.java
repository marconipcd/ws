package com.digital.opuserp.view.modulos.acesso.contratos_acesso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsArea;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.addon.charts.model.Stacking;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ConsumoView extends Window {
	
	
	
	TextField tfBusca;
	Table tbChamados;
	Button btSalvar;
	Button btCancelar;
	Button btImprimir;
	Button btDesconectar;
	Button btAtualizar;
	
	Integer valorInicial;
	
	String username;
	Item item;
	Window window;
		
	VerticalLayout vl;
	Refresher refresher;
	
	public ConsumoView(boolean modal, boolean center, String username){
		
		super("Consumo ( "+username+" )");
		
		this.username = username;
	
		setWidth("1100px");
		setHeight("400px");
		
		setModal(modal);
		if(center){
			center();
		}
		
		setClosable(false);
						
		vl = new VerticalLayout(){
			{
				setSizeFull();
				setMargin(true);
				
				HorizontalLayout hlButtons2 = new HorizontalLayout();
				hlButtons2.setSpacing(true);
				hlButtons2.setMargin(true);
				hlButtons2.setStyleName("hl_buttons_bottom");
	
			
				addComponent(hlButtons2);
				setComponentAlignment(hlButtons2, Alignment.TOP_RIGHT);
				
				addComponent(buildChart());
				setExpandRatio(chart, 1f);
			
				
				HorizontalLayout hlButtons = new HorizontalLayout();
				hlButtons.setSpacing(true);
				hlButtons.setMargin(true);
				hlButtons.setStyleName("hl_buttons_bottom");
				hlButtons.addComponent(buildBtCancelar());
				//hlButtons.addComponent(buildBtSalvar());
				
				addComponent(hlButtons);
				setComponentAlignment(hlButtons, Alignment.BOTTOM_RIGHT);
				
				
				}
			
				
		};
		setContent(vl);
		
		addExtension(buildRefresher());	
	}
	
	private Chart chart;
	private Chart buildChart(){
    	chart = new Chart();
    	
    	chart.getConfiguration().getChart().setType(ChartType.AREA);
        chart.getConfiguration().getyAxis().setTitle("Qtd");
        chart.getConfiguration().getxAxis().setTitle("Hora");      
     
        chart.setWidth("100%");
        chart.setHeight("90%");
        
        pegarDown();
        
        return chart;
    }
	
	public Refresher buildRefresher(){
		
		
		refresher = new Refresher();
		refresher.setRefreshInterval(15000);
		refresher.addListener(new Refresher.RefreshListener() {
			
			@Override
			public void refresh(Refresher source) {
								
					pegarDown();
		        	
		        	
			}

			
		});
		
		return refresher;
	}
	
	List<Series> series = new ArrayList<Series>();
	private void pegarDown() {
		
		SimpleDateFormat sdf = 
				new SimpleDateFormat("HH:mm:ss");		
		Random ran = new Random();			
		
		String nome = sdf.format(new Date());
		Number valor = ran.nextInt();
		
		PlotOptionsArea areP = new PlotOptionsArea();
		areP.setStacking(Stacking.NONE);
		
    	ListSeries serie = new ListSeries();
    	serie.setName(nome);
    	serie.addData(valor);
    
    	chart.getConfiguration().addSeries(serie);
    	chart.drawChart();
    	//chart.

	}
	
	
	private Button buildBtCancelar() {
		btCancelar = new Button("Cancelar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}	
		});
		
		ShortcutListener clTb = new ShortcutListener("Cancelar", ShortcutAction.KeyCode.ESCAPE, null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btCancelar.click();
			}
		};
		btCancelar.addShortcutListener(clTb);
		
		return btCancelar;
	}

	
	
	
	private Button buildBtAtualizar() {
		btAtualizar = new Button("Atualizar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
							
				//vl.replaceComponent(tbChamados, buildTbChamados());
				//vl.setExpandRatio(tbChamados, 1);
			}
		});
		return btAtualizar;	
	}
	
	
}
