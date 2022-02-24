package com.digital.opuserp.view.modulos.configuracoes.config_sistema.usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.digital.opuserp.dao.UsuarioDAO;
import com.digital.opuserp.domain.Usuario;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.view.modulos.configuracoes.config_sistema.usuario.UsuarioEditor.UsuarioEvent;
import com.digital.opuserp.view.util.Notify;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare.Greater;
import com.vaadin.data.util.filter.Compare.Less;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

public class UsuarioView extends VerticalLayout{
	//
	JPAContainer<Usuario> container;

	Button btNovo;
	Button btEditar;
	Button btDeletar;
	Button btGerenciarEmpresas;
	Button btPermissoesGerais;
	TextField tfBusca;	
	Table tbUsuario;
	HorizontalLayout hlFloat ;
	
	private Label lbRegistros;
	
	HorizontalLayout hlTableUser;
	
	ShortcutListener slNovo;
	ShortcutListener slEditar;
	
	ComboBox cbStatus;
	
	public UsuarioView(boolean act) {
		
		if(act){
		
		//Definindo o sizeFull para o componente root verticalLayout
		setSizeFull();

		// Instaciando Bot�es
		btNovo = buildBtNovo();
		btEditar = buildBtEditar();
		btDeletar = buildBtDeletar();
		btGerenciarEmpresas = buildBtGerenciarEnpresas();
		btPermissoesGerais =  buildBtPermissoesGerais();
		
		
		
		// Instaciando TextFild
		tfBusca = buildTfBusca();
		
		
		
						
		//configurando o layout dos butoons
		HorizontalLayout  hlButtons = new HorizontalLayout();
		hlButtons.addComponent(btNovo);
		hlButtons.addComponent(btEditar);
		hlButtons.addComponent(btDeletar);
		hlButtons.addComponent(btGerenciarEmpresas);
		hlButtons.addComponent(btPermissoesGerais);
		
	
		
		//configurando o layout do tfBUsca
		HorizontalLayout hlBusca = new HorizontalLayout();
		hlBusca.setWidth("100%");		
		
		cbStatus = new ComboBox();
		cbStatus.setTextInputAllowed(false);
		cbStatus.setNullSelectionAllowed(false); 
		cbStatus.addItem("ATIVO");
		cbStatus.addItem("INATIVO");
		cbStatus.select("ATIVO");
		
		cbStatus.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				addFilter(tfBusca.getValue(), cbStatus.getValue().toString());
			}
		});
		
		hlBusca.addComponent(cbStatus);
		hlBusca.addComponent(tfBusca);
		hlBusca.setExpandRatio(tfBusca, 1f);

			
		hlTableUser = new HorizontalLayout();
		hlTableUser.addComponent(buildTbUsuario());
		//hlTableUser.setExpandRatio(tbUsuario, 2);
		hlTableUser.setSizeFull();
		
		//Adicionando componentes ao layout root 
		addComponent(hlButtons);
		setComponentAlignment(hlButtons, Alignment.TOP_RIGHT);
		addComponent(hlBusca);
		addComponent(hlTableUser);
		//addComponent(buildLbRegistros());
		
		hlFloat = new HorizontalLayout();
		hlFloat.setWidth("100%");
		hlFloat.addComponent(buildLbRegistros());
		Label lbLegend = new Label("F2 - Novo Cadastro | ENTER - Editar");
		lbLegend.setWidth("250px");
		hlFloat.addComponent(lbLegend);
		hlFloat.setComponentAlignment(lbRegistros, Alignment.BOTTOM_LEFT);
		hlFloat.setComponentAlignment(lbLegend, Alignment.BOTTOM_RIGHT);
		
		
		addComponent(hlFloat);
		
		setExpandRatio(hlTableUser, 2);			
		}
	}
	
	public void refresh(){		
		hlTableUser.replaceComponent(tbUsuario, buildTbUsuario());
		hlTableUser.setExpandRatio(tbUsuario, 1);		
	}
	
	private Label buildLbRegistros(){
		lbRegistros = new Label(String.valueOf(container.size()) + " Registros Encontrados");
		return lbRegistros;
	}
	
	public void buildShortcurEvents(Component c){
		
		if(c instanceof UsuarioView)
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
	
	public void addFilter(String s, String status) {
		container.removeAllContainerFilters();
		container.getEntityProvider().setQueryModifierDelegate(null);
		container.setApplyFiltersImmediately(false);
		
		container.addContainerFilter(Filters.eq("status", status));		
		container.sort(new String[] {"username"}, new boolean[]{true});
		
		if(s != null && !s.equals("") && !s.isEmpty()){

			Object[] collums = tbUsuario.getVisibleColumns();	
		
			List<Filter> filtros = new ArrayList<Filter>();		
			
			
			try {
				Integer cod = Integer.valueOf(s);			
				
					for(Object c:collums){		 			
						if(!tbUsuario.isColumnCollapsed(c.toString()) && container.getType(c.toString()) == Integer.class){					   	
							filtros.add(new Like(c.toString(), "%"+cod+"%", false));
						}			
						if(!tbUsuario.isColumnCollapsed(c.toString()) && container.getType(c.toString()) == String.class){					   	
							filtros.add(new Like(c.toString(), "%"+s+"%", false));
						}		
		
					}
				
				
			} catch (Exception e) {
			

					try {				
					
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
						
						Date date1 = sdf.parse(s+" 00:01:00");
						Date date2 = sdf.parse(s+" 23:59:00");
						
							for(Object c:collums){		 
									if(!tbUsuario.isColumnCollapsed(c.toString()) && container.getType(c.toString()) == Date.class){
										
										if(!c.toString().equals("data_nascimento")){
											filtros.add(Filters.and( new Greater(c.toString(), date1),new Less(c.toString(), date2)));
										}else{
											Date date = sdf2.parse(s);
											filtros.add(Filters.eq(c.toString(), date));
										}
									
								}
							}	
						
						
					} catch (Exception e2) {
						for(Object c:collums){		 
							
							if(!tbUsuario.isColumnCollapsed(c.toString()) && container.getType(c.toString()) == String.class){					   	
								filtros.add(new Like(c.toString(), "%"+s+"%", false));
							}	
						}
					}
				}
//			}
			
			container.addContainerFilter(Filters.or(filtros));
		
		}
				
		container.applyFilters();
		container.sort(new String[] {"username"}, new boolean[]{true});
		
		if(lbRegistros != null){
			hlFloat.replaceComponent(lbRegistros, buildLbRegistros());
		}
	}
	
	private Button buildBtNovo(){
		btNovo = new Button("Novo", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				final BeanItem <Usuario> item = new BeanItem <Usuario>(new Usuario());
				
				UsuarioEditor usuarioEditor = new UsuarioEditor(item, "Cadastro de usuário",true);
				usuarioEditor.addListerner(new UsuarioEditor.UsuarioListerner() {
					
					@Override
					public void onClose(UsuarioEvent event) {
						if(event.isConfirm()){
							
							Usuario u_novo = item.getBean();							
							boolean check = UsuarioDAO.save(u_novo);
							
							if(check){
								Notify.Show("Usuário cadastrado com sucesso!", Notify.TYPE_SUCCESS);
								refresh();
							}
							
						}
					}
				});
				
				
				getUI().addWindow(usuarioEditor);
				usuarioEditor.center();
				usuarioEditor.setModal(true);
			}
			
		});
			
		return btNovo;		
	}
	
	private Button buildBtEditar(){
		btEditar = new Button("Editar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(tbUsuario.getValue() != null){
					UsuarioEditor usuarioEditor = new UsuarioEditor(tbUsuario.getItem(tbUsuario.getValue()), "Editar usuário",true);
					usuarioEditor.addListerner(new UsuarioEditor.UsuarioListerner() {
						
						@Override
						public void onClose(UsuarioEvent event) {
							if(event.isConfirm()){
								EntityItem<Usuario> entityItemUsuario = (EntityItem<Usuario>)event.getItem();
								Usuario u = entityItemUsuario.getEntity();
								
								boolean check = UsuarioDAO.save(u);
								
								if(check){
									Notify.Show("Usuário alterado com sucesso", Notify.TYPE_SUCCESS);
									refresh();
								}
							}
						}
					});
															
					getUI().addWindow(usuarioEditor);						
				}			
			}
		});
		
		
		return btEditar;
	}

	
	private Button buildBtDeletar(){
		btDeletar = new Button("Deletar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(tbUsuario.getValue() != null){				
										
					GenericDialog gd = new GenericDialog("Confirme para Continuar!", "Deseja realmente excluir este usuario?", true,true);
					gd.addListerner(new GenericDialog.DialogListerner() {
						
						@Override
						public void onClose(DialogEvent event) {
							if(event.isConfirm()){
								
								Notify.Show("Ainda não é possível remover um usuário! ", Notify.TYPE_WARNING);
								//refresh();

							}
						}
					});
					
					gd.addCloseListener(new Window.CloseListener() {
						
						@Override
						public void windowClose(CloseEvent e) {
							tbUsuario.focus();
						}
					});
					
					getUI().addWindow(gd);				
				}	
			}
		});
		btDeletar.setEnabled(false);
		return btDeletar;
	}
	private Button  buildBtGerenciarEnpresas(){
		btGerenciarEmpresas = new Button("Gerenciar Empresas", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(tbUsuario.getValue()!= null){				

				Integer codUsuairo = Integer.parseInt(tbUsuario.getItem(tbUsuario.getValue()).getItemProperty("id").toString());
					
					Integer cod = Integer.parseInt(tbUsuario.getItem(tbUsuario.getValue()).getItemProperty("id").toString());
					String username = tbUsuario.getItem(tbUsuario.getValue()).getItemProperty("username").toString();
					String password= tbUsuario.getItem(tbUsuario.getValue()).getItemProperty("password").toString();
					String funcao = tbUsuario.getItem(tbUsuario.getValue()).getItemProperty("funcao").toString();
					
					GerenciarEmpresa empresa = new GerenciarEmpresa("Gerenciar Empresa", true,							
															new Usuario(cod, username, password, funcao));
					
					empresa.addCloseListener(new Window.CloseListener() {
						
						@Override
						public void windowClose(CloseEvent e) {
							tbUsuario.focus();
						}
					});
					
					getUI().addWindow(empresa);	
				}
			}
		});
		btGerenciarEmpresas.setEnabled(false);
		return btGerenciarEmpresas;
	}

	private Button  buildBtPermissoesGerais(){
		btPermissoesGerais = new Button("Permissões Gerais", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(tbUsuario.getValue()!= null){
					
					EntityItem<Usuario> entityItemUsuario = (EntityItem<Usuario>)tbUsuario.getItem(tbUsuario.getValue()); 

					PermissoesGeraisEditor modulos = new PermissoesGeraisEditor("Permissões Gerais", true,entityItemUsuario.getEntity());
					
					modulos.addCloseListener(new Window.CloseListener() {
						
						@Override
						public void windowClose(CloseEvent e) {
							tbUsuario.focus();
						}
					});
					
					modulos.addCloseListener(new Window.CloseListener() {
						
						@Override
						public void windowClose(CloseEvent e) {
							refresh();
						}
					});
					
					getUI().addWindow(modulos);					
				}		
			}
		});
		btPermissoesGerais.setEnabled(false);
		return btPermissoesGerais;
	}

	private TextField buildTfBusca(){
		tfBusca = new TextField();
		tfBusca.setWidth("100%");
		tfBusca.setInputPrompt("Buscar...");
		
		tfBusca.addTextChangeListener(new FieldEvents.TextChangeListener() {
			
			@Override
			public void textChange(TextChangeEvent event) {
				addFilter(event.getText(), cbStatus.getValue().toString());			
			}
		});
		
		return tfBusca;
	}
	
	
	public JPAContainer<Usuario> buildContainer(){
		container = new JPAContainerFactory().makeBatchable(Usuario.class, ConnUtil.getEntity());
		
		if(tfBusca != null && tfBusca.getValue() != null && !tfBusca.getValue().equals("")){
			addFilter(tfBusca.getValue(), cbStatus.getValue().toString());	
		}else{
			container.setAutoCommit(false);
			container.removeAllContainerFilters();
			container.setApplyFiltersImmediately(false);
			container.addContainerFilter(Filters.eq("status", "ATIVO"));			
			container.applyFilters();
			
			container.sort(new String[] {"username"}, new boolean[]{true});
		}
		
		
		return container;
	}

	private Table buildTbUsuario(){
				
		tbUsuario = new Table(null,buildContainer());
		tbUsuario.setSizeFull();		
		tbUsuario.setVisibleColumns(new Object[]{"id","username","funcao","email","setor","visualizar_todos_crm","status"});
		tbUsuario.setColumnHeader("visualizar_todos_crm","Visualiza todos Crms");
		tbUsuario.setColumnHeader("setor","Setor");
		tbUsuario.setColumnHeader("email","E-mail");
		tbUsuario.setColumnHeader("username","Nome");
		tbUsuario.setColumnHeader("funcao","Função");
		tbUsuario.setColumnHeader("status","Status");
		tbUsuario.setColumnHeader("id","ID");
		tbUsuario.setSelectable(true);	 
//		tbUsuario.setMultiSelect(true);	 
		tbUsuario.setColumnCollapsingAllowed(true);
		tbUsuario.setColumnCollapsed("id",true);
		tbUsuario.setColumnCollapsible("username",false);

		//tbUsuario.setColumnExpandRatio("funcao", 1f);
		
		
		
		tbUsuario.setImmediate(true);
		tbUsuario.addListener(new  Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(event.getProperty().getValue() != null)
				{
					btDeletar.setEnabled(true);
					btEditar.setEnabled(true);
					btGerenciarEmpresas.setEnabled(true);
					btPermissoesGerais.setEnabled(true);
				}else{
					btDeletar.setEnabled(false);
					btEditar.setEnabled(false);
					btGerenciarEmpresas.setEnabled(false);
					btPermissoesGerais.setEnabled(false);
				}
			}
		});
		
//		tbUsuario.addListener(new ItemClickEvent.ItemClickListener() {
//			
//			@Override
//			public void itemClick(ItemClickEvent event) {
//				if(event.isDoubleClick()){
//					
//					UsuarioEditor usuarioEditor = new UsuarioEditor(tbUsuario.getItem(tbUsuario.getValue()), "Editar Usuario");					
//					usuarioEditor.addListener(new UsuarioCancelListener() {
//						
//						public void cancelarAlteracao(UsuarioCancelEvent event) {
//							container.discard();
//							hlTableUser.replaceComponent(tbUsuario, buildTbUsuario());
//							tbUsuario.focus();
//						}
//					});				
//					usuarioEditor.addListerner(new UsuarioSalvedListener() {
//						
//						@Override
//						public void usuarioSalvo(UsuarioSalveEvent event) {		
//									 
//							container.commit();		
//							hlTableUser.replaceComponent(tbUsuario, buildTbUsuario());
//							tbUsuario.focus();
//						}
//					});
//					
//					usuarioEditor.addCloseListener(new Window.CloseListener() {
//						
//						@Override
//						public void windowClose(CloseEvent e) {
//							tbUsuario.focus();
//						}
//					});
//					
//					getUI().addWindow(usuarioEditor);
//					usuarioEditor.center();
//					usuarioEditor.setModal(true);	
//				}
//			}
//		});		
		return tbUsuario;
	}
	
	

	

}
