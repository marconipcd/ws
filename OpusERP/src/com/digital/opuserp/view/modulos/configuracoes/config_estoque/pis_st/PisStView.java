package com.digital.opuserp.view.modulos.configuracoes.config_estoque.pis_st;

import java.util.ArrayList;
import java.util.List;

import com.digital.opuserp.dao.GerenciarModuloDAO;
import com.digital.opuserp.domain.Issqn;
import com.digital.opuserp.domain.Pisst;
import com.digital.opuserp.interfaces.GenericView;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.view.modulos.configuracoes.config_estoque.issqn.IssqnEditor;
import com.digital.opuserp.view.modulos.configuracoes.config_estoque.issqn.IssqnEditor.IssqnEvent;
import com.digital.opuserp.view.modulos.configuracoes.config_estoque.pis_st.PisStEditor.PisStEvent;
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
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

public class PisStView extends VerticalLayout implements GenericView {

	//JPA
	JPAContainer<Pisst> container;
	
	Table tb;
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
	
	public PisStView(boolean act){
		
		if(act){
		setSizeFull();
		
		gmDAO = new GerenciarModuloDAO();
		
		HorizontalLayout hlButons = new HorizontalLayout();
		hlButons.addComponent(BuildbtTransferencia());
		hlButons.addComponent(BuildbtAceitar());
		hlButons.addComponent(BuildbtCancelar());
		
		addComponent(hlButons);
		setComponentAlignment(hlButons, Alignment.TOP_RIGHT);
		
		addComponent(new HorizontalLayout(){
			{
				setWidth("100%");
				addComponent(buildTfbusca());
				setExpandRatio(tfBusca, 1.0f);
			}
		});
		
		addComponent(buildTbGeneric());

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
	
	public JPAContainer<Pisst> buildContainer(){
		container = JPAContainerFactory.makeBatchable(Pisst.class, ConnUtil.getEntity());
		container.setAutoCommit(false);	
		container.sort(new Object[]{"descricao"}, new boolean[]{true});
		
		return container;
	}
	

	public void buildShortcurEvents(Component c){
		
		if(c instanceof PisStView)
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
	
	@Override
	public Table buildTbGeneric() {
		tb = new Table(null, buildContainer());
		tb.setSizeFull();
		tb.setSelectable(true);
		tb.setColumnCollapsingAllowed(true);
		tb.setColumnCollapsed("id", true);		
						
		tb.setColumnHeader("id", "Cod");
		tb.setColumnHeader("descricao", "Descri????o");		
		tb.setColumnHeader("tipo_pis", "Tipo Pis");		
		tb.setColumnHeader("base_calculo", "Base C??lculo");
		tb.setColumnHeader("aliquota", "Aliquota");		
		tb.setColumnHeader("valor_pis", "Valor Pis");
					
		tb.setVisibleColumns(new Object[] {"id","descricao","tipo_pis","base_calculo","aliquota","valor_pis"});		
		
		tb.setImmediate(true);
		tb.addValueChangeListener(new Property.ValueChangeListener() {
			
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
		
	tb.addItemClickListener(new ItemClickEvent.ItemClickListener() {
		
		@Override
		public void itemClick(ItemClickEvent event) {
			if(event.isDoubleClick()  && tb.getValue() != null){
				
				PisStEditor cEditor = new PisStEditor(tb.getItem(tb.getValue()), "Editar PisSt", true);
				cEditor.addListerner(new PisStEditor.PisStListerner() {
					
					@Override
					public void onClose(PisStEvent event) {
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
		
	@Override
	public TextField buildTfbusca() {
		tfBusca = new TextField();
		tfBusca.setWidth("100%");
		tfBusca.setInputPrompt("Buscar...");
		tfBusca.setTextChangeEventMode(TextChangeEventMode.LAZY);
		tfBusca.focus();
		tfBusca.addTextChangeListener(new FieldEvents.TextChangeListener() {
			
			@Override
			public void textChange(TextChangeEvent event) {
				addFilter(event.getText());
			}
		});
		return tfBusca;
	}

	
	@Override
	public void addFilter(String s) {
		container.removeAllContainerFilters();
		
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

		container.addContainerFilter(Filters.or(filtros));
		container.applyFilters();
		container.sort(new Object[]{"descricao"}, new boolean[]{true});
		hlFloat.replaceComponent(lbRegistros, buildLbRegistros());
	}

	@Override
	public Button BuildbtTransferencia() {
		btNovo = new Button("Novo", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
					final BeanItem<Pisst> newItem = new BeanItem<Pisst>(new Pisst());
								
					PisStEditor cEditor = new PisStEditor(newItem, "Cadastrar PisSt", true);
					cEditor.addListerner(new PisStEditor.PisStListerner() {
						
						@Override
						public void onClose(PisStEvent event) {
							if(event.isConfirm()){
								
								try {
									Pisst cAdd = newItem.getBean();									
									container.addEntity(cAdd);	
									container.commit();
									hlFloat.replaceComponent(lbRegistros, buildLbRegistros());
									Notify.Show("PisSt Cadastrado com Sucesso!", Notify.TYPE_SUCCESS);
								} catch (Exception e) {
									e.printStackTrace();																
									Notify.Show("ERRO: "+e.getMessage(), Notify.TYPE_ERROR);
								}
								
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
				
			}
		});
		return btNovo;
	}

	@Override
	public Button BuildbtAceitar() {
		btEditar = new Button("Editar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				//if(gmDAO.checkPermissaoEmpresaSubModuloUsuario(codSubModulo, OpusERP4UI.getEmpresa().getId(), OpusERP4UI.getUsuarioLogadoUI().getId(), "Alterar"))				
			//	{
				
				PisStEditor cEditor = new PisStEditor(tb.getItem(tb.getValue()), "Editar PisSt", true);
				cEditor.addListerner(new PisStEditor.PisStListerner() {
					
					@Override
					public void onClose(PisStEvent event) {
						if(event.isConfirm()){
							container.commit(); 
							hlFloat.replaceComponent(lbRegistros, buildLbRegistros());
							Notify.Show("PisSt Alterado com Sucesso!", Notify.TYPE_SUCCESS);
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
			//		Notification.show("Aten????o", "Voc?? n??o Possui Permiss??o para Editar um Contrato de Acesso", Type.ERROR_MESSAGE);
				//}
			}
		});
		btEditar.setEnabled(false);
			
		return btEditar;
	}

	@Override
	public Button BuildbtCancelar() {
		btExcluir = new Button("Excluir", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
			
				
				final Integer codContratoAcesso = Integer.parseInt(tb.getItem(tb.getValue()).getItemProperty("id").toString());			

				GenericDialog gDialog = new GenericDialog("Confirme para Continuar!", "Voc?? deseja realmente Excluir o  PisSt Selecionado?", true, true);
					gDialog.addListerner(new GenericDialog.DialogListerner() {
						
						@Override
						public void onClose(DialogEvent event) {
							if(event.isConfirm()){
								container.removeItem(tb.getValue());
								container.commit();
								hlFloat.replaceComponent(lbRegistros, buildLbRegistros());
								Notify.Show("PisSt Excluido com Sucesso!", Notify.TYPE_SUCCESS);
							}
						}
					});
				
				getUI().addWindow(gDialog);
				
				
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
