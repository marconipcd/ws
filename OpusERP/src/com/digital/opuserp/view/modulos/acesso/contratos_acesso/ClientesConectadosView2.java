package com.digital.opuserp.view.modulos.acesso.contratos_acesso;

import java.util.List;

import com.digital.opuserp.domain.Concentrador;
import com.digital.opuserp.domain.RadAcct;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.HuaweiUtil;
import com.digital.opuserp.util.MikrotikUtil;
import com.digital.opuserp.view.util.Notify;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class ClientesConectadosView2 extends Window {
	
	JPAContainer<RadAcct> container;
	
	TextField tfBusca;
	Table tb;
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
	
	public ClientesConectadosView2(boolean modal, boolean center){
		
		super("Cliente Conectados");
		
	
		
		setWidth("1100px");
		setHeight("600px");
		
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
//				hlButtons2.addComponent(buildBtImprimir());
				hlButtons2.addComponent(buildBtAtualizar());
				//hlButtons2.addComponent(buildBtDesconectar());
				
				addComponent(hlButtons2);
				setComponentAlignment(hlButtons2, Alignment.TOP_RIGHT);
				
				
				addComponent(buildTextField());
				addComponent(buildTbChamados());
				addComponent(buildLbRegistros());
				setExpandRatio(tb, 2);
				
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
	}
	
	public JPAContainer<RadAcct> buildJpaContainer(){
		container = JPAContainerFactory.make(RadAcct.class, ConnUtil.getEntity());
		container.addContainerFilter(Filters.eq("username", username));
		container.sort(new String[]{"radacctid"}, new boolean[]{false});
		container.applyFilters(); 
		return container;
	}
	
	
List<String[]> clientes;
	
	
	private Component buildTextField2(){
		
		TextField txtBusca = new TextField("");
		txtBusca.setWidth("100%");
		txtBusca.addTextChangeListener(new FieldEvents.TextChangeListener() {
			
			@Override
			public void textChange(TextChangeEvent event) {
										
				tb.removeAllItems();
				for (String[] s : clientes) {
					
					if(s[0].contains(event.getText()) || s[1].contains(event.getText()) || 
						s[2].contains(event.getText()) ||s[3].contains(event.getText()) ||
						s[4].contains(event.getText()) || s[5].contains(event.getText()) ){
					
						//clientes1.add(new String[]{s[0],s[1],s[2],s[3],s[4],s[5]});	
						tb.addItem(new Object[]{s[0],s[1],s[2],s[3],s[4],s[5]}, tb.getItemIds().size()+1);
					}							
				}						
			}
		});
		
		return txtBusca;
	}
	
	public Table buildTbChamados(){
		tb = new Table();
		tb.setWidth("100%");
		tb.setHeight("100%");
		
		clientes = HuaweiUtil.pegarClientesConectados();
		
		tb.addContainerProperty("userId", String.class, "");
		tb.addContainerProperty("username", String.class, "");
		tb.addContainerProperty("interfaces", String.class, "");
		tb.addContainerProperty("ip_address", String.class, "");
		tb.addContainerProperty("mac", String.class, "");
		tb.addContainerProperty("vlan", String.class, "");
		
		tb.addGeneratedColumn("v", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(final Table source,final  Object itemId, Object columnId) {
				
				Button btVisualizar = new Button("visualizar", new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						
						String username = source.getItem(itemId).getItemProperty("username").getValue().toString();
						String vlan = source.getItem(itemId).getItemProperty("vlan").getValue().toString();
						VisualizarContratoInfoTecnica visuali = new VisualizarContratoInfoTecnica(Integer.parseInt(username),vlan, "Informações do contrato ( "+username+" )",true);
						
						getUI().addWindow(visuali);						
					
					}
				});
				
				btVisualizar.setStyleName(Reindeer.BUTTON_LINK);	
				
				return btVisualizar;
			}
		});
		
//		tb.addGeneratedColumn("t", new Table.ColumnGenerator() {
//			
//			@Override
//			public Object generateCell(final Table source,final  Object itemId, Object columnId) {
//				
//				Button btVisualizar = new Button("tempo real", new Button.ClickListener() {
//					
//					@Override
//					public void buttonClick(ClickEvent event) {
//						
////						String username = source.getItem(itemId).getItemProperty("username").getValue().toString();
////						String url_consumo = "http://172.17.0.71/consumo2/?u="+username;
////						
////						Window w = new Window();
////						w.setWidth("781px");
////						w.setHeight("561px");
////						w.setCaption("Tráfego em tempo real ( "+username+" )");
////						w.center();
////						w.setResizable(false);
////						
////												
////						final Embedded consumo = new Embedded(null,new ExternalResource(url_consumo));
////				        consumo.setType(Embedded.TYPE_BROWSER);
////				        consumo.setSizeFull();
////				        
////				        w.setContent(consumo);
////						
////						getUI().addWindow(w);						
//					
//					}
//				});
//				
//				btVisualizar.setStyleName(Reindeer.BUTTON_LINK);	
//				
//				return btVisualizar;
//			}
//		});
		
		
		for (String[] s: clientes) {
			//if(s[1] != "" && !s[1].isEmpty() ){
				tb.addItem(new Object[]{s[0],s[1],s[2],s[3],s[4],s[5]}, tb.getItemIds().size()+1);
			//}
		}
				 
		return tb;
	}
	public TextField buildTextField() {
		tfBusca = new TextField();
		tfBusca.setWidth("100%");
		tfBusca.setInputPrompt("Buscar...");
		tfBusca.setTextChangeEventMode(TextChangeEventMode.LAZY);
		tfBusca.focus();
		tfBusca.addListener(new FieldEvents.TextChangeListener() {
			
			@Override
			public void textChange(TextChangeEvent event) {
				addFilter(event.getText());
			}
		});
		return tfBusca;
	}
	

	public void addFilter(String se) {
		
		tb.removeAllItems();
		for (String[] s : clientes) {
			
			if(s[0].contains(se) || s[1].contains(se) || 
				s[2].contains(se) ||s[3].contains(se) ||
				s[4].contains(se) || s[5].contains(se) ){
			
				//clientes1.add(new String[]{s[0],s[1],s[2],s[3],s[4],s[5]});	
				tb.addItem(new Object[]{s[0],s[1],s[2],s[3],s[4],s[5]}, tb.getItemIds().size()+1);
			}							
		}	
		
		vl.replaceComponent(lbRegistros, buildLbRegistros());
	}
		
	private Button buildBtCancelar() {
		btCancelar = new Button("Fechar", new Button.ClickListener() {
			
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

	
	private Button buildBtImprimir() {
		btImprimir = new Button("Imprimir", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
//				try{
//					window = new Window();
//					window.setCaption("Imprimir Histórico de Acesso");
//			        window.setWidth("800px");
//			        window.setHeight("600px");
//			        window.setResizable(false);
//			        window.center();
//			        window.setModal(true);
//			        window.setStyleName("disable_scroolbar");		
//			        window.setCloseShortcut(KeyCode.ESCAPE, null);
//			        
//			        
//			        AcessoCliente ac = (AcessoCliente) item.getItemProperty("id").getValue();
//			        Cliente cliente = (Cliente) item.getItemProperty("cliente").getValue();
////			        Integer codAc = Integer.parseInt(tbChamados.getItem(tbChamados.getValue()).getItemProperty("id").getValue().toString()); 
////			        String nomeCliente = tbChamados.getItem(tbChamados.getValue()).getItemProperty("cliente.nome_razao").getValue().toString();
////			        String codCliente = tbChamados.getItem(tbChamados.getValue()).getItemProperty("cliente.id").getValue().toString();
//			        StreamResource resource = new StreamResource(new ImprimirHistoricoAcesso(ac.getId()), "Histórico de Acesso "+cliente.getId()+" - "+cliente.getNome_razao()+".pdf");
//			        resource.getStream();			        
//			        resource.setMIMEType("application/pdf");
//			        resource.setCacheTime(0);
//			        
//			        Embedded e = new Embedded();
//			        e.setSizeFull();
//			        e.setType(Embedded.TYPE_BROWSER); 
//			        e.setSource(resource);			     
//			        window.setContent(e);
//			        getUI().addWindow(window);
//			        window.focus();
//			        
//				}catch(Exception e){
//					System.out.println("Deu ERRADO!");
//					e.printStackTrace();
//				}
				
			}
		});
		return btImprimir;
	}
	
	private Button buildBtAtualizar() {
		btAtualizar = new Button("Atualizar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
							
				vl.replaceComponent(tb, buildTbChamados());
				vl.setExpandRatio(tb, 1);
				
				vl.replaceComponent(lbRegistros, buildLbRegistros());
			}
		});
		return btAtualizar;	
	}
	
	private Button buildBtDesconectar() {
		btDesconectar = new Button("Desconectar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(item != null){
					Concentrador base = (Concentrador) item.getItemProperty("base").getValue();
					
					boolean check = false;
					if(base.getTipo().equals("mikrotik")){					
							check = MikrotikUtil.desconectarCliente(base.getUsuario(), base.getSenha(), base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()), username);
					}
					
					if(base.getTipo().equals("huawei")){		
							check  = HuaweiUtil.desconectarCliente(item.getItemProperty("login").getValue().toString());
					}

					
					if(check){						
						Notify.Show("O Cliente foi Desconectado Com Sucesso!", Notify.TYPE_SUCCESS);
					}else{
						Notify.Show("O Cliente já esta Desconectado!", Notify.TYPE_NOTICE);
					}					
				}
				vl.replaceComponent(tb, buildTbChamados());
				vl.setExpandRatio(tb, 1);
			}
		});
		return btDesconectar;	
	}
	
	private Label lbRegistros;
	private Label buildLbRegistros(){
		lbRegistros =  new Label(tb.getItemIds().size()+" Registros encontrados");
		return lbRegistros;
	}
	
}
