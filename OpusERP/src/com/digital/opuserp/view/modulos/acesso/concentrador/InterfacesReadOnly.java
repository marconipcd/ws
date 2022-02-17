package com.digital.opuserp.view.modulos.acesso.concentrador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.digital.opuserp.domain.BasesAnalitico;
import com.digital.opuserp.domain.InterfaceWireless;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.exporterpdf.PdfExporter;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class InterfacesReadOnly extends Window {
	
	TextField tfBusca;
	Table tbInterface;
	Table tbBase;
	
	PdfExporter pdfExporterBase;
	PdfExporter pdfExporterInterface;
	
	JPAContainer<InterfaceWireless> containerInterface;
	JPAContainer<BasesAnalitico> containerConcentradores;

	List<InterfaceWireless> interfaces;
	
	Label lbQtdConcentradores;
	Label lbQtdInterface;
	
	public InterfacesReadOnly(boolean modal, boolean center){
		
		super("Analítico Concentradores");
		
		this.interfaces = interfaces;

		setWidth("1226px");
		setHeight("535px");

		setIcon(new ThemeResource("icons/search-32.png"));
		setModal(modal);
		if(center){
			center();
		}
		
		setClosable(true);

		setContent(new VerticalLayout(){
			{
				setSizeFull();
				setMargin(true);
				
				TabSheet ts_principal = new TabSheet();
				 ts_principal.setSizeFull();
				 
				VerticalLayout vl1 = new VerticalLayout(){
					{
						
						HorizontalLayout hlBusca = new HorizontalLayout(){
							{
								setStyleName("margin-5-h");
								setWidth("100%");
								addComponent(buildDataFiltroBase());
								addComponent(buildTxtFiltroBase());
								setExpandRatio(txtBuscaBase, 0.5f);
								
							}
						};
						addComponent(hlBusca);
						setExpandRatio(hlBusca, 0.18f);
						
						VerticalLayout lvTable = new VerticalLayout(){
							{
								setSizeFull();
								addComponent(buildTbBase());								
							}
						};
						addComponent(lvTable);
						setExpandRatio(lvTable, 2.1f);
						
						
						HorizontalLayout hlPdf = new HorizontalLayout(){
							{
								setSizeFull();
								
								addComponent(buildLbConecntradores());
								setComponentAlignment(lbQtdConcentradores, Alignment.MIDDLE_LEFT);
								
								addComponent(buildPdfExporterBase());
								setComponentAlignment(pdfExporterBase, Alignment.MIDDLE_RIGHT);
							}
						};
						addComponent(hlPdf);
						setExpandRatio(hlPdf, 0.2f);	
					}
				};
				
				vl1.setSizeFull();
				 
				VerticalLayout vl2 = new VerticalLayout(){
					{
						
						HorizontalLayout hlBusca = new HorizontalLayout(){
							{
								setStyleName("margin-5-h");
								setWidth("100%");
								addComponent(buildDataFiltroInterface());
								addComponent(buildTxtFiltroInterface());
								setExpandRatio(txtBuscaInterface, 0.5f);
								
							}
						};
						addComponent(hlBusca);
						setExpandRatio(hlBusca, 0.18f);
						
						VerticalLayout lvTable = new VerticalLayout(){
							{
								setSizeFull();
								addComponent(buildTbInterface());								
							}
						};
						addComponent(lvTable);
						setExpandRatio(lvTable, 2.1f);
						
						
						HorizontalLayout hlPdf = new HorizontalLayout(){
							{
								setSizeFull();
								
								addComponent(buildLbInterface());
								setComponentAlignment(lbQtdInterface, Alignment.MIDDLE_LEFT);
								
								addComponent(buildPdfExporterInterface());
								setComponentAlignment(pdfExporterInterface, Alignment.MIDDLE_RIGHT);
							}

							
						};
						addComponent(hlPdf);
						setExpandRatio(hlPdf, 0.2f);	
					}
				};
				//vl2.addStyleName("margin-top-10");
				vl2.setSizeFull();
				
				
				
				TabSheet.Tab tBase = ts_principal.addTab(vl1, "Concentradores");

				TabSheet.Tab tInterface = ts_principal.addTab(vl2, "Interfaces Wireless");

				
				addComponent(ts_principal); 
				 
				
			}
		});
	}
	
	private Label buildLbInterface() {
		lbQtdInterface = new Label(containerInterface.size()+" Registros encontrados");
		return lbQtdInterface;
	}
	private Label buildLbConecntradores() {
		lbQtdConcentradores= new Label(containerConcentradores.size()+" Registros encontrados");
		return lbQtdConcentradores;
	}

	TextField txtBuscaInterface;
	private TextField buildTxtFiltroInterface(){
		txtBuscaInterface = new TextField();
		txtBuscaInterface.addTextChangeListener(new FieldEvents.TextChangeListener() {
			
			@Override
			public void textChange(TextChangeEvent event) {
				
				addFilterInterface(dtxDataInterface.getValue(), event.getText());				
			}
		});
		txtBuscaInterface.setWidth("100%");
		
		return txtBuscaInterface;
	}
	
	TextField txtBuscaBase;
	private TextField buildTxtFiltroBase(){
		txtBuscaBase = new TextField();
		txtBuscaBase.addTextChangeListener(new FieldEvents.TextChangeListener() {
			
			@Override
			public void textChange(TextChangeEvent event) {
				
				addFilterBase(dtxDataBase.getValue(), event.getText());				
			}
		});
		txtBuscaBase.setWidth("100%");
		
		return txtBuscaBase;
	}
	
	DateField dtxDataInterface;
	private DateField buildDataFiltroInterface(){
		dtxDataInterface = new DateField();
		dtxDataInterface.setDateFormat("dd/MM/yyyy");
		dtxDataInterface.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				addFilterInterface(dtxDataInterface.getValue(), txtBuscaInterface.getValue());
			}
		});
		
		return dtxDataInterface;
	}	
	
	DateField dtxDataBase;
	private DateField buildDataFiltroBase(){
		dtxDataBase = new DateField();
		dtxDataBase.setDateFormat("dd/MM/yyyy");
		dtxDataBase.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				addFilterBase(dtxDataBase.getValue(), txtBuscaBase.getValue());
			}
		});
		
		return dtxDataBase;
	}	
	
	private void addFilterInterface(Date d, String s){
			try{
				containerInterface.removeAllContainerFilters();
				//container.setApplyFiltersImmediately(false); 
				
				if(d != null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String dt_1 = sdf.format(d)+" 01:00:00";
					String dt_2 = sdf.format(d)+" 00:59:00";
								
					DateTime dt1 = new DateTime(sdf.parse(dt_1));
					DateTime dt2 = new DateTime(sdf.parse(dt_2));
					
					containerInterface.addContainerFilter(Filters.gt("data", dt1.toDate()));
					containerInterface.addContainerFilter(Filters.lt("data", dt2.plusDays(1).toDate()));
				}
				
				if(s != null && !s.equals("") && !s.isEmpty()){

					Object[] collums = tbInterface.getVisibleColumns();		
					List<Filter> filtros = new ArrayList<Filter>();		
					
					try {
						Integer cod = Integer.parseInt(s);
						
						for(Object c:collums){		 			
							if(!tbInterface.isColumnCollapsed(c.toString()) && containerInterface.getType(c.toString()) == Integer.class){					   	
								filtros.add(new Like(c.toString(), "%"+cod+"%", false));
							}			
							
							if(!tbInterface.isColumnCollapsed(c.toString()) && containerInterface.getType(c.toString()) == String.class){					   	
								filtros.add(new Like(c.toString(), "%"+s+"%", false));
							}	
						}
						
					} catch (Exception e) {
							
							for(Object c:collums){					
													
								if(!tbInterface.isColumnCollapsed(c.toString()) && containerInterface.getType(c.toString()) == String.class){					   	
									filtros.add(new Like(c.toString(), "%"+s+"%", false));			
								}

							}
					}
					containerInterface.addContainerFilter(Filters.or(filtros));
				}
				
				lbQtdInterface.setValue(containerInterface.size()+" Registros encontrados");
				//container.addContainerFilter(Filters.and(Filters.gteq("data", dt2.toDate()),Filters.lteq("data", dt1.toDate())));				
				//container.applyFilters();
			}catch(Exception e){
				e.printStackTrace();
			}
		
	}
	private void addFilterBase(Date d, String s){
		try{
			containerConcentradores.removeAllContainerFilters();
			//container.setApplyFiltersImmediately(false); 
			
			if(d != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String dt_1 = sdf.format(d)+" 01:00:00";
				String dt_2 = sdf.format(d)+" 00:59:00";
							
				DateTime dt1 = new DateTime(sdf.parse(dt_1));
				DateTime dt2 = new DateTime(sdf.parse(dt_2));
				
				containerConcentradores.addContainerFilter(Filters.gt("data", dt1.toDate()));
				containerConcentradores.addContainerFilter(Filters.lt("data", dt2.plusDays(1).toDate()));
			}
			
			if(s != null && !s.equals("") && !s.isEmpty()){

				Object[] collums = tbBase.getVisibleColumns();		
				List<Filter> filtros = new ArrayList<Filter>();		
				
				try {
					Integer cod = Integer.parseInt(s);
					
					for(Object c:collums){		 			
						if(!tbInterface.isColumnCollapsed(c.toString()) && containerConcentradores.getType(c.toString()) == Integer.class){					   	
							filtros.add(new Like(c.toString(), "%"+cod+"%", false));
						}			
						
						if(!tbInterface.isColumnCollapsed(c.toString()) && containerConcentradores.getType(c.toString()) == String.class){					   	
							filtros.add(new Like(c.toString(), "%"+s+"%", false));
						}	
					}
					
				} catch (Exception e) {
						
						for(Object c:collums){					
												
							if(!tbInterface.isColumnCollapsed(c.toString()) && containerConcentradores.getType(c.toString()) == String.class){					   	
								filtros.add(new Like(c.toString(), "%"+s+"%", false));			
							}

						}
				}
				containerConcentradores.addContainerFilter(Filters.or(filtros));
			}
			
			lbQtdConcentradores.setValue(containerConcentradores.size()+" Registros encontrados");
			//container.addContainerFilter(Filters.and(Filters.gteq("data", dt2.toDate()),Filters.lteq("data", dt1.toDate())));				
			//container.applyFilters();
		}catch(Exception e){
			e.printStackTrace();
		}
	
}
	
	public JPAContainer<InterfaceWireless> buildContainerInterface() {
		containerInterface = JPAContainerFactory.makeBatchable(InterfaceWireless.class, ConnUtil.getEntity());
		containerInterface.setAutoCommit(false);
		containerInterface.sort(new Object[]{"nome"}, new boolean[]{true});
		
		return containerInterface;
	}
	
	public JPAContainer<BasesAnalitico> buildContainerBase() {
		containerConcentradores = JPAContainerFactory.makeBatchable(BasesAnalitico.class, ConnUtil.getEntity());
		containerConcentradores.setAutoCommit(false);
		containerConcentradores.sort(new Object[]{"nome"}, new boolean[]{true});
		
		return containerConcentradores;
	}
	
	public Table buildTbBase(){
		
		tbBase = new Table(null, buildContainerBase());

		tbBase.setSizeFull();
		tbBase.setSelectable(true);			
		tbBase.setSelectable(true);
		tbBase.setColumnCollapsingAllowed(true);
	

		tbBase.setVisibleColumns(new Object[]{"nome","uptime","routeros","firmware","cpu","architecture_name","board_name","data"});

		
		tbBase.setColumnHeader("nome", "Nome");
		tbBase.setColumnHeader("uptime", "Uptime");
		tbBase.setColumnHeader("routeros", "RouterOs");
		tbBase.setColumnHeader("firmware", "Firmware");

		tbBase.setColumnHeader("cpu", "CPU");		
		tbBase.setColumnHeader("architecture_name", "Arquitetura");
		tbBase.setColumnHeader("board_name", "Modelo");

		tbBase.setColumnHeader("data", "Data");
	
		tbBase.setColumnCollapsible("nome", false);
		
		
		
		setCloseShortcut(KeyCode.ESCAPE, null);
		tbBase.focus();
		    		
		return tbBase;
	}
	
	public Table buildTbInterface(){
		
		tbInterface = new Table(null, buildContainerInterface());

		tbInterface.setSizeFull();
		tbInterface.setSelectable(true);			
		tbInterface.setSelectable(true);
		tbInterface.setColumnCollapsingAllowed(true);
	
		tbInterface.setVisibleColumns(new Object[]{"nome","ssid","mac","channel","authenticated_clientes","distancia","ccq","data"});
		tbInterface.setColumnHeader("nome", "Nome");
		tbInterface.setColumnHeader("uptime", "Uptime");		
		tbInterface.setColumnHeader("ssid", "SSID");
		tbInterface.setColumnHeader("mac", "MAC");
		tbInterface.setColumnHeader("channel", "Channel");
		tbInterface.setColumnHeader("authenticated_clientes", "Clientes Autenticados");
		tbInterface.setColumnHeader("distancia", "Distancia");
		tbInterface.setColumnHeader("ccq", "CCQ");
		tbInterface.setColumnHeader("data", "Data");
	
		
		//tbInterface.setcon
		
		tbInterface.setColumnCollapsible("nome", false);
		
		
		
		
		
		setCloseShortcut(KeyCode.ESCAPE, null);
		tbInterface.focus();
		    		
		return tbInterface;
	}
	
	public PdfExporter buildPdfExporterBase(){
		pdfExporterBase = new PdfExporter(tbBase);		
		pdfExporterBase.setCaption("Imprimir");
		pdfExporterBase.setWithBorder(true);
		pdfExporterBase.setHeader("Analítico Concentradores");
		
		
		
		return pdfExporterBase;
	}
	
	public PdfExporter buildPdfExporterInterface(){
		pdfExporterInterface= new PdfExporter(tbInterface);		
		pdfExporterInterface.setCaption("Imprimir");
		pdfExporterInterface.setWithBorder(true);
		pdfExporterInterface.setHeader("Analítico Interfaces");
		
		
		
		return pdfExporterInterface;
	}
		
}
