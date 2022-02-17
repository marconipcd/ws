package com.digital.opuserp.view.modulos.configuracoes.config_cadastro.categoria;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.digital.opuserp.dao.CategoriasDAO;
import com.digital.opuserp.domain.Categorias;
import com.digital.opuserp.interfaces.GenericEditor;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.view.modulos.cadastro.transportadoras.TransportadoraEditor.TransportadoraEvent;
import com.digital.opuserp.view.util.Notify;
//import com.github.wolfie.refresher.Refresher;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CategoriaEditor extends Window implements GenericEditor {

	Item item;
	Button btSalvar;
	Button btCancelar;
	FormLayout flPrincipal;
	FieldGroup fieldGroup;
	VerticalLayout vlRoot;
	TextField tfNome;
	//Refresher refresher;
	
	public CategoriaEditor(Item item, String title, boolean modal){
		this.item = item;
		
		
		setCaption(title);
		setModal(modal);
		setResizable(false);
		setClosable(false);
		center();
		
		vlRoot = new VerticalLayout();	
		vlRoot.setWidth("100%");
		vlRoot.setMargin(true);
		vlRoot.setStyleName("border-form");
		
		
		setContent(new VerticalLayout(){
			{
				setWidth("100%");
				setMargin(true);
				addComponent(vlRoot);
				
				
				HorizontalLayout hlButtons = new HorizontalLayout();
				hlButtons.setStyleName("hl_buttons_bottom");
				hlButtons.setSpacing(true);
				hlButtons.setMargin(true);
				hlButtons.addComponent(buildBtCancelar());
				hlButtons.addComponent(buildBtSalvar());
				
				addComponent(hlButtons);
				setComponentAlignment(hlButtons, Alignment.BOTTOM_RIGHT);
			}
		});
		buildLayout();
		
		//addExtension(buildRefresher());	
	}

//	public Refresher buildRefresher(){
//		
//		
//		refresher = new Refresher();
//		refresher.setRefreshInterval(5000);
//		refresher.addListener(new Refresher.RefreshListener() {
//			
//			@Override
//			public void refresh(Refresher source) {
//				
//				BeanItem<Categorias> entityItem = (BeanItem<Categorias>) fieldGroup.getItemDataSource();
//				Categorias c = entityItem.getBean();
//				c.setNome(tfNome.getValue());
//				CategoriasDAO.save(c);
//				Notify.Show("Cadastro Salvo Automaticamente!", Notify.TYPE_WARNING);
//			}
//		});
//		
//		return refresher;
//	}
	public void buildLayout(){
		
		fieldGroup = new FieldGroup(item);
					
		vlRoot.addComponent(
				new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);
														
					tfNome = new TextField ("Nome");				
					tfNome.setWidth("100%");				
					//tfCod.setStyleName("caption-align");
					tfNome.setNullRepresentation("");
					tfNome.setRequired(true);
					tfNome.setMaxLength(200);
					tfNome.setImmediate(true); 
					tfNome.focus();
					addComponent(tfNome);
					setExpandRatio(tfNome, 2);	
					tfNome.setTextChangeEventMode(TextChangeEventMode.LAZY);
					tfNome.addBlurListener(new FieldEvents.BlurListener() {
						
						@Override
						public void blur(BlurEvent event) {
							
							if(tfNome.getValue()!=null && tfNome.getValue().equals("")){
								((TextField) (event.getComponent())).setValue(event.getComponent().toString().toUpperCase());								
							}
								}
						});
					
					fieldGroup.bind(tfNome,"nome");
				}
			});
		
		
//		vlRoot.addComponent(
//				new FormLayout(){					
//				{
//					setStyleName("form-cutom");
//					setMargin(true);
//					setSpacing(true);
//														
//					ComboBox tfCod = new ComboBox("Status");					
//					tfCod.setNullSelectionAllowed(false);
//					tfCod.addItem("ATIVO");
//					tfCod.addItem("INATIVO");
//					//tfCod.setStyleName("caption-align");					
//					tfCod.setRequired(true);					
//					addComponent(tfCod);		
//					
//					fieldGroup.bind(tfCod, "status");
//				}
//			});

		
		
	}

	@Override
	public Button buildBtSalvar() {
		
		btSalvar = new Button("OK", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(fieldGroup.isValid()){
					try {				
						if(item.getItemProperty("id").getValue() == null){
							fieldGroup.commit();				
							fireEvent(new CategoriaEvent(getUI(), item, true));					
							Notification.show("Categoria Cadastrada com Sucesso!");
						}else{
							if(fieldGroup.isModified()){
								fieldGroup.commit();				
								fireEvent(new CategoriaEvent(getUI(), item, true));					
								Notification.show("Categoria Alterada com Sucesso!");
							}
						}
						close();
					} catch (Exception e) {					
						//LogErrorDAO.add(new LogError(null, "CategoriaEditor.java", "buildBtSalvar()", e.toString(),null,OpusERP4UI.EMPRESA_SELECIONADA, OpusERP4UI.USUARIO_LOGADO));					
						Notification.show("Não foi Possivel Salvar as Alterações!");
					}
				}else{
					
					for (Field<?> field: fieldGroup.getFields()) {
						
						if(!field.isValid()){
							field.addStyleName("invalid-txt");
						}else{
							field.removeStyleName("invalid-txt");
						}
					}
					
					Notification.show("Não é Possivel Salvar, Verifique os Campos Obrigatórios/Inválidos e Tente Novamente!");
				}
				
			}
		});
		

		ShortcutListener slbtOK = new ShortcutListener("Ok", ShortcutAction.KeyCode.ENTER,null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btSalvar.click();
			}
		};
		
		btSalvar.addShortcutListener(slbtOK);
		
		btSalvar.setStyleName("default");
		return btSalvar;
	}

	@Override
	public Button buildBtCancelar() {
		btCancelar = new Button("Cancelar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(!fieldGroup.isModified()){
					fieldGroup.discard();				
					fireEvent(new CategoriaEvent(getUI(), item, false));
					close();
				}else{
					GenericDialog gDialog = new GenericDialog("Confirme para Continuar!", "Deseja Salvar as Informações Alteradas?", true, true);
					gDialog.setCaptionCANCEL("Sair sem Salvar!");
					gDialog.setCaptionOK("Salvar");
					
					gDialog.addListerner(new GenericDialog.DialogListerner() {
						
						@Override
						public void onClose(DialogEvent event) {
							if(event.isConfirm()){
								try {
									fieldGroup.commit();				
									fireEvent(new CategoriaEvent(getUI(), item, true));									
									if(item.getItemProperty("id").getValue() == null){
										
										Notification.show("Categoria Cadastrado com Sucesso!");
									}else{								
										Notification.show("Categoria Alterado com Sucesso!");
									}
									close();
								} catch (Exception e) {
									System.out.println("Erro ao cadastrar ou alterar : "+e.getMessage()+".\n Causado por: "+e.getCause());
									Notification.show("Não foi Possivel Salvar as Alterações!");
								}
							}else{							
								fieldGroup.discard();				
								fireEvent(new TransportadoraEvent(getUI(), item, false));
								close();						
							}
						}
					});					
					
					getUI().addWindow(gDialog);
					
				}				
			}
		});
		
		ShortcutListener slbtCancelar = new ShortcutListener("Cancelar", ShortcutAction.KeyCode.ESCAPE,null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btCancelar.click();
			}
		};
		
		btCancelar.addShortcutListener(slbtCancelar);
		
		
		return btCancelar;
	}
	
	
	public void addListerner(CategoriaListerner target){
		try {
			Method method = CategoriaListerner.class.getDeclaredMethod("onClose", CategoriaEvent.class);
			addListener(CategoriaEvent.class, target, method);
		} catch (Exception e) {
			System.out.println("Método não Encontrado!");
		}
	}
	public void removeListerner(CategoriaListerner target){
		removeListener(CategoriaEvent.class, target);
	}
	public static class CategoriaEvent extends Event{
		
		private Item item;
		private boolean confirm;
		
		public CategoriaEvent(Component source, Item item, boolean confirm) {
			super(source);
			this.item = item;
			this.confirm = confirm;			
		}

		public Item getItem() {
			return item;
		}	

		public boolean isConfirm() {
			return confirm;
		}		
	}
	public interface CategoriaListerner extends Serializable{
		public void onClose(CategoriaEvent event);
	}

	
}
