package com.digital.opuserp.view.modulos.configuracoes.config_acesso.beneficio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.digital.opuserp.OpusERP4UI;
import com.digital.opuserp.dao.ContratosAcessoDAO;
import com.digital.opuserp.dao.GerenciarModuloDAO;
import com.digital.opuserp.domain.Beneficio;
import com.digital.opuserp.domain.Swith;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.util.Real;
import com.digital.opuserp.view.modulos.configuracoes.config_acesso.contratos.ContratosEditor;
import com.digital.opuserp.view.modulos.configuracoes.config_acesso.contratos.ContratosEditor.ContratoEvent;
import com.digital.opuserp.view.modulos.configuracoes.config_acesso.contratos.ContratosView;
import com.digital.opuserp.view.util.Notify;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

public class BeneficioView extends VerticalLayout{

	JPAContainer<Beneficio> container;
	
	Table tb;
	ComboBox cbStatus;
	TextField tfBusca;
	Button btNovo;
	Button btEditar;
	Button btExcluir;
	HorizontalLayout hlFloat;
	private Label lbRegistros;
	
	ShortcutListener slNovo;
	ShortcutListener slEditar;
	
	GerenciarModuloDAO gmDAO;
	private Integer codSubModulo;
	
	public BeneficioView(boolean act){
		
		if(act){
				setSizeFull();
				
				gmDAO = new GerenciarModuloDAO();
				
				HorizontalLayout hlButons = new HorizontalLayout();
				hlButons.addComponent(BuildbtNovo());
				hlButons.addComponent(BuildbtEditar());
				hlButons.addComponent(BuildbtExcluir());
				
				addComponent(hlButons);
				setComponentAlignment(hlButons, Alignment.TOP_RIGHT);
				
				addComponent(new HorizontalLayout(){
					{
						setWidth("100%");
						//addComponent(buildComboStatus());
						addComponent(buildTfbusca());
						setExpandRatio(tfBusca, 1.0f);
					}
				});
				
				addComponent(buildTbGeneric());
		//		addComponent(buildLbRegistros());
		
				hlFloat = new HorizontalLayout();
				hlFloat.setWidth("100%");
				hlFloat.addComponent(buildLbRegistros());
				Label lbLegend = new Label("F2 - Novo Cadastro | ENTER - Editar");
				lbLegend.setWidth("250px");
				hlFloat.addComponent(lbLegend);
				hlFloat.setComponentAlignment(lbRegistros, Alignment.BOTTOM_LEFT);
				hlFloat.setComponentAlignment(lbLegend, Alignment.BOTTOM_RIGHT);
				
				addComponent(hlFloat);
				
				setExpandRatio(tb, 1);
		}
	}

	public Label buildLbRegistros(){
		lbRegistros = new Label(String.valueOf(container.size()) + " Registros Encontrados");
		return lbRegistros;
	}
	
	public JPAContainer<Beneficio> buildContainer(){
		container = JPAContainerFactory.makeBatchable(Beneficio.class, ConnUtil.getEntity());
		container.setAutoCommit(false);
				
//		if(cbStatus.getValue().toString() != null){
//			container.addContainerFilter(Filters.eq("status", cbStatus.getValue().toString()));
//		}
				
		container.sort(new Object[]{"nome"}, new boolean[]{true});
		
		return container;
	}
	

	public void buildShortcurEvents(Component c){
		
		if(c instanceof ContratosView)
		{
			btNovo.addShortcutListener(buildShortCutNovo());
			btEditar.addShortcutListener(buildShortCutEditar());
		}else{
			if(btNovo != null || slNovo != null){				
				btNovo.removeShortcutListener(slNovo);
			}
			
			if(btEditar != null || slEditar != null){
				btEditar.removeShortcutListener(slEditar);			
			}
		}
	}
	public ShortcutListener buildShortCutNovo(){
		slNovo = new ShortcutListener("Novo",ShortcutAction.KeyCode.F2,null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btNovo.click();
			}
		};
		return slNovo;
	}
	public ShortcutListener buildShortCutEditar(){
		slEditar = new ShortcutListener("Editar",ShortcutAction.KeyCode.ENTER,null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btEditar.click();
			}
		};
		return slEditar;
	}
	
	public Table buildTbGeneric() {
		tb = new Table(null, buildContainer()){
			@Override
			protected String formatPropertyValue(Object rowId, Object colId,
					Property<?> property) {
												
				if(colId.equals("beneficio_adesao") || colId.equals("beneficio_comodato") || 
						colId.equals("isencao_taxa_instalacao") || colId.equals("isencao_manu_servico")){
					
					if(tb.getItem(rowId).getItemProperty(colId).getValue() != null){						
						return Real.formatDbToString(tb.getItem(rowId).getItemProperty(colId).getValue().toString());				
					}					
				}
				
				return super.formatPropertyValue(rowId, colId, property);			
			}
		};
		tb.setSizeFull();
		tb.setSelectable(true);
		tb.setColumnCollapsingAllowed(true);
		tb.setColumnCollapsed("id", true);
							
		tb.setColumnHeader("id", "Cod");
		tb.setColumnHeader("nome", "Nome");
		tb.setColumnHeader("beneficio_adesao", "Benefício de Adesão");
		tb.setColumnHeader("beneficio_comodato", "Benefício de Comodato");		
		tb.setColumnHeader("isencao_taxa_instalacao", "Isenção Taxa Instalação");		
		tb.setColumnHeader("isencao_manu_servico", "Isenção Manutenção de Serviço");
				
		tb.setColumnAlignment("beneficio_adesao", Align.RIGHT);
		tb.setColumnAlignment("beneficio_comodato", Align.RIGHT);
		tb.setColumnAlignment("isencao_taxa_instalacao", Align.RIGHT);
		tb.setColumnAlignment("isencao_manu_servico", Align.RIGHT);
		
		tb.setVisibleColumns(new Object[] {"id","nome","beneficio_adesao","beneficio_comodato",
				"isencao_taxa_instalacao","isencao_manu_servico"});
		
		tb.setImmediate(true);
		tb.addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					btEditar.setEnabled(true);
					btExcluir.setEnabled(true);
				}else{
					btEditar.setEnabled(false);
					btExcluir.setEnabled(false);
				}
			}
		});
		
	tb.addListener(new ItemClickEvent.ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.isDoubleClick()  && tb.getValue() != null){
					
					ContratosEditor cEditor = new ContratosEditor(tb.getItem(tb.getValue()), "Editar Tipo de Plano", true);
					cEditor.addListerner(new ContratosEditor.ContratoListerner() {
						
						@Override
						public void onClose(ContratoEvent event) {
							if(event.isConfirm()){
								container.commit();
							}
						}
					});					
					getUI().addWindow(cEditor);				
				}
			}
		});
		
		return tb;
	}

	public ComboBox buildComboStatus(){
		cbStatus = new ComboBox();
		cbStatus.setNullSelectionAllowed(false);
		cbStatus.addItem("ATIVO");
		cbStatus.addItem("INATIVO");
		cbStatus.setImmediate(true);
		cbStatus.select("ATIVO");
		cbStatus.addListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				addFilter(tfBusca.getValue());				
			}
		});		
		return cbStatus;	
	}
	
	
	public TextField buildTfbusca() {
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

	public void addFilter(String s) {
		container.removeAllContainerFilters();
		
		container.addContainerFilter(Filters.eq("status", cbStatus.getValue().toString()));		
		
		Object[] collums = tb.getVisibleColumns();		
		List<Filter> filtros = new ArrayList<Filter>();	

		try {
			Integer cod = Integer.valueOf(s);
		
			for(Object c:collums){		 			
				if(!tb.isColumnCollapsed(c.toString()) && container.getType(c.toString()) == Integer.class){					   	
					filtros.add(new Like(c.toString(), "%"+cod+"%", false));
				}			
				if(!tb.isColumnCollapsed(c.toString()) && container.getType(c.toString()) == String.class){					   	
					filtros.add(new Like(c.toString(), "%"+s+"%", false));
				}	
			}
		
		} catch (Exception e) {
	
			for(Object c:collums){		 			
				if(!tb.isColumnCollapsed(c.toString()) && container.getType(c.toString()) == String.class){					   	
					filtros.add(new Like(c.toString(), "%"+s+"%", false));
				}			
			}
		}

//		for(Object c:collums){		 			
//		if(!tb.isColumnCollapsed(c.toString()) && container.getType(c.toString()) == String.class){					   	
//			filtros.add(new Like(c.toString(), "%"+s+"%", false));
//		}			
//	}
		container.addContainerFilter(Filters.or(filtros));
		container.applyFilters();
		container.sort(new Object[]{"nome"}, new boolean[]{true});
		hlFloat.replaceComponent(lbRegistros, buildLbRegistros());
	}

	public Button BuildbtNovo() {
		btNovo = new Button("Novo", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				//if(gmDAO.checkPermissaoEmpresaSubModuloUsuario(codSubModulo, OpusERP4UI.getEmpresa().getId(), OpusERP4UI.getUsuarioLogadoUI().getId(), "Cadastrar"))				
			//	{
				
						final BeanItem<Beneficio> newItem = new BeanItem<Beneficio>(new Beneficio());
										
						ContratosEditor cEditor = new ContratosEditor(newItem, "Cadastrar Benefício", true);
						cEditor.addListerner(new ContratosEditor.ContratoListerner() {
							
							@Override
							public void onClose(ContratoEvent event) {
								if(event.isConfirm()){
									
									try {
										Beneficio cAdd = newItem.getBean();
										
										container.addEntity(cAdd);	
										container.commit();
										hlFloat.replaceComponent(lbRegistros, buildLbRegistros());
										Notify.Show("Benefício Cadastrado com Sucesso!", Notify.TYPE_SUCCESS);
									} catch (Exception e) {
										e.printStackTrace();																
										Notify.Show("ERRO: "+e.getMessage(), Notify.TYPE_ERROR);
									}
									
								}else{
								}
							}
						});
						
						
						cEditor.addCloseListener(new Window.CloseListener() {
							
							@Override
							public void windowClose(CloseEvent e) {
								tfBusca.focus();							
							}
						});
						
						getUI().addWindow(cEditor);
				
				//}else{
			//		Notification.show("Atenção", "Você não Possui Permissão para Cadastrar um Contrato de Acesso", Type.ERROR_MESSAGE);
				//}
			}
		});
		return btNovo;
	}

	public Button BuildbtEditar() {
		btEditar = new Button("Editar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				//if(gmDAO.checkPermissaoEmpresaSubModuloUsuario(codSubModulo, OpusERP4UI.getEmpresa().getId(), OpusERP4UI.getUsuarioLogadoUI().getId(), "Alterar"))				
			//	{
				
				ContratosEditor cEditor = new ContratosEditor(tb.getItem(tb.getValue()), "Editar Tipo de Plano", true);
				cEditor.addListerner(new ContratosEditor.ContratoListerner() {
					
					@Override
					public void onClose(ContratoEvent event) {
						if(event.isConfirm()){
							container.commit(); 
							hlFloat.replaceComponent(lbRegistros, buildLbRegistros());
							Notify.Show("Tipo de Plano Alterado com Sucesso!", Notify.TYPE_SUCCESS);
						}
					}
				});
				
				getUI().addWindow(cEditor);
				
				cEditor.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
						tb.focus();							
					}
				});
				
				//}else{
			//		Notification.show("Atenção", "Você não Possui Permissão para Editar um Contrato de Acesso", Type.ERROR_MESSAGE);
				//}
			}
		});
		btEditar.setEnabled(false);
			
		return btEditar;
	}

	public Button BuildbtExcluir() {
		btExcluir = new Button("Excluir", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				//if(gmDAO.checkPermissaoEmpresaSubModuloUsuario(codSubModulo, OpusERP4UI.getEmpresa().getId(), OpusERP4UI.getUsuarioLogadoUI().getId(), "Excluir"))				
			//	{
				
				final Integer codContratoAcesso = Integer.parseInt(tb.getItem(tb.getValue()).getItemProperty("id").toString());
				
				ContratosAcessoDAO paDAO = new ContratosAcessoDAO();						
				if(paDAO.allowRemove(codContratoAcesso)==false){
					Notify.Show("A Tipo de Plano Selecionado está em Uso!", Notify.TYPE_ERROR);
				}else{

				GenericDialog gDialog = new GenericDialog("Confirme para Continuar!", "Você deseja realmente Excluir o Tipo de Plano Selecionado?", true, true);
					gDialog.addListerner(new GenericDialog.DialogListerner() {
						
						@Override
						public void onClose(DialogEvent event) {
							if(event.isConfirm()){
								container.removeItem(tb.getValue());
								container.commit();
								hlFloat.replaceComponent(lbRegistros, buildLbRegistros());
								Notify.Show("Tipo de Plano Excluido com Sucesso!", Notify.TYPE_SUCCESS);
							}
						}
					});
				
				getUI().addWindow(gDialog);
				
				
			}
			//}else{
			//		Notification.show("Atenção", "Você não Possui Permissão para Excluir um Contrato de Acesso", Type.ERROR_MESSAGE);
			//	}
		   }
		});
		btExcluir.setEnabled(false);
		return btExcluir;
		
	}

	public Integer getCodSubModulo() {
		return codSubModulo;
	}

	public void setCodSubModulo(Integer codSubModulo) {
		this.codSubModulo = codSubModulo;
	}
	
}