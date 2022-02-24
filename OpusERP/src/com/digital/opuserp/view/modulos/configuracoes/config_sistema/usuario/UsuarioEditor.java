package com.digital.opuserp.view.modulos.configuracoes.config_sistema.usuario;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.digital.opuserp.interfaces.GenericEditor;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.view.util.Notify;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class UsuarioEditor extends Window implements GenericEditor {
	
	Item item;
	Button btSalvar;
	Button btCancelar;
	FormLayout flPrincipal;
	FieldGroup fieldGroup;
	VerticalLayout vlRoot;
	

	ComboBox cb;
	ComboBox cbStatus;
	
	
	public UsuarioEditor(Item item, String title, boolean modal){
		this.item = item;
		
		setCaption(title);
		setModal(modal);
		setResizable(false);
		setClosable(false);
		center();
		
		vlRoot = new VerticalLayout();	
		vlRoot.setWidth("100%");
		vlRoot.setMargin(true);
		vlRoot.addStyleName("border-form");
		
		setContent(new VerticalLayout(){
			{
				setWidth("100%");
				setMargin(true);
				addComponent(vlRoot);
								
				HorizontalLayout hlButtons = new HorizontalLayout();
				hlButtons.setSpacing(true);
				hlButtons.setMargin(true);
				hlButtons.addComponent(buildBtCancelar());
				hlButtons.addComponent(buildBtSalvar());
				
				addComponent(hlButtons);
				setComponentAlignment(hlButtons, Alignment.BOTTOM_RIGHT);
			}
		});
				
		buildLayout();
	}
	
	
	
	
	private void configLayout(){
		//1366x768
		//if(OpusERP4UI.browser.getScreenWidth() >= 1024 && OpusERP4UI.browser.getScreenHeight() >= 768){
				setWidth("832px");
				setHeight("430px");		
		//}
	}
	
	public void buildLayout(){
		
		fieldGroup = new FieldGroup(item);
			
		vlRoot.addComponent(new FormLayout(){					
				{
					
					setMargin(true);
					setSpacing(true);
					addStyleName("form-cutom");						
									
					TextField txtUsuario = (TextField)fieldGroup.buildAndBind("Usuário", "username");				
					txtUsuario.setWidth("200px");				
					txtUsuario.addStyleName("caption-align-planos");
					txtUsuario.setNullRepresentation("");					
					txtUsuario.setRequired(true);
											
					addComponent(txtUsuario); 
									
				}
			});

		vlRoot.addComponent(new FormLayout(){					
				{
					
					setMargin(true);
					setSpacing(true);
					addStyleName("form-cutom");						
									
					PasswordField txtSenha = new PasswordField("Senha");				
					txtSenha.setWidth("200px");				
					txtSenha.addStyleName("caption-align-planos");
					txtSenha.setNullRepresentation("");					
					txtSenha.setRequired(true);
											
					addComponent(txtSenha);
					
					fieldGroup.bind(txtSenha, "password");			
									
				}
			});
		
		vlRoot.addComponent(new FormLayout(){					
					{						
						setMargin(true);
						setSpacing(true);
						addStyleName("form-cutom");						
						
						ComboBox cbFuncao = new ComboBox("Função");		
						cbFuncao.setTextInputAllowed(false);
						cbFuncao.addStyleName("caption-align-planos");				
						cbFuncao.addStyleName("align-currency");
						cbFuncao.setNullSelectionAllowed(false);
						cbFuncao.setRequired(true);
						cbFuncao.addItem("admin");
						cbFuncao.addItem("operador");
						cbFuncao.addItem("tecnico");
						
						addComponent(cbFuncao);
												
						fieldGroup.bind(cbFuncao, "funcao");						
					}
		});
		
		vlRoot.addComponent(new FormLayout(){					
			{						
				setMargin(true);
				setSpacing(true);
				addStyleName("form-cutom");						
				
				ComboBox cbSetor = new ComboBox("Setor");	
				cbSetor.setTextInputAllowed(false);
				cbSetor.addStyleName("caption-align-planos");				
				cbSetor.addStyleName("align-currency");
				cbSetor.setNullSelectionAllowed(false);
				cbSetor.setRequired(true);
				cbSetor.addItem("DIRETORIA");
				cbSetor.addItem("FINANCEIRO");
				cbSetor.addItem("COMERCIAL");
				cbSetor.addItem("SUPORTE");
				cbSetor.addItem("DESENVOLVIMENTO");
				
				addComponent(cbSetor);
										
				fieldGroup.bind(cbSetor, "setor");						
			}
		});
		
		vlRoot.addComponent(	new FormLayout(){					
			{
				
				setMargin(true);
				setSpacing(true);
				addStyleName("form-cutom");						
				
				TextField txtEmail = new TextField("Email");	
				
				txtEmail.setWidth("245px");
				txtEmail.setNullRepresentation("");		
				txtEmail.addStyleName("caption-align-planos");						
				txtEmail.setRequired(true);					
				addComponent(txtEmail);
				setExpandRatio(txtEmail, 2);
				
				fieldGroup.bind(txtEmail, "email");
				
			}
		});
	
		vlRoot.addComponent(	new FormLayout(){					
			{
				
				setMargin(true);
				setSpacing(true);
				addStyleName("form-cutom");						
				
				ComboBox cbVisualizaTodosCrms = new ComboBox("Visualizar Todos os Crms");				
				cbVisualizaTodosCrms.setTextInputAllowed(false);
				cbVisualizaTodosCrms.addStyleName("caption-align-planos");				
				cbVisualizaTodosCrms.addStyleName("align-currency");
				cbVisualizaTodosCrms.setNullSelectionAllowed(false);
				cbVisualizaTodosCrms.setRequired(true);
				cbVisualizaTodosCrms.addItem("SIM");
				cbVisualizaTodosCrms.addItem("NAO");				
				addComponent(cbVisualizaTodosCrms);
				
				
				fieldGroup.bind(cbVisualizaTodosCrms, "visualizar_todos_crm");
				
			}
		});
		
		vlRoot.addComponent(	new FormLayout(){					
					{
						
						setMargin(true);
						setSpacing(true);
						addStyleName("form-cutom");						
						
						cbStatus = new ComboBox("Status");	
						cbStatus.setTextInputAllowed(false);
						cbStatus.addStyleName("caption-align-planos");				
						cbStatus.addStyleName("align-currency");
						cbStatus.setNullSelectionAllowed(false);
						cbStatus.setRequired(true);
						cbStatus.addItem("ATIVO");
						cbStatus.addItem("INATIVO");
						cbStatus.select("ATIVO");
						addComponent(cbStatus);
						setExpandRatio(cbStatus, 2);
						
						fieldGroup.bind(cbStatus, "status");
						
					}
				});
		
	}

	@Override
	public Button buildBtSalvar() {
		btSalvar = new Button("OK", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(fieldGroup.isValid()){
						salvar();					
				}else{					
					Notify.Show_Invalid_Submit_Form();
				}
				
			}

			
		});
		
		ShortcutListener clTb = new ShortcutListener("Salvar", ShortcutAction.KeyCode.ENTER, null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btSalvar.click();
			}
		};
		btSalvar.addShortcutListener(clTb);
		btSalvar.setStyleName("default");
		return btSalvar;
	}
	
	private void salvar() {
		try {						
			fieldGroup.commit();				
			fireEvent(new UsuarioEvent(getUI(), item, true));						
			close();
		} catch (CommitException e) {						
			e.printStackTrace();
		}
	}

	@Override
	public Button buildBtCancelar() {
		btCancelar = new Button("Cancelar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(!fieldGroup.isModified()){
					fieldGroup.discard();				
					fireEvent(new UsuarioEvent(getUI(), item, false));
					close();
				}else{
					GenericDialog gDialog = new GenericDialog("Confirme para Continuar!", "Deseja Salvar as Informações Alteradas?", true, true);
					gDialog.setCaptionCANCEL("Sair sem Salvar!");
					gDialog.setCaptionOK("Salvar");
					
					gDialog.addListerner(new GenericDialog.DialogListerner() {
						
						@Override
						public void onClose(DialogEvent event) {
								if(event.isConfirm()){
									salvar();
								}							
						}
					});					
					
					getUI().addWindow(gDialog);
					
				}				
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
	
	
	public void addListerner(UsuarioListerner target){
		try {
			Method method = UsuarioListerner.class.getDeclaredMethod("onClose", UsuarioEvent.class);
			addListener(UsuarioEvent.class, target, method);
		} catch (Exception e) {
			System.out.println("Método não Encontrado!");
		}
	}
	public void removeListerner(UsuarioListerner target){
		removeListener(UsuarioEvent.class, target);
	}
	public static class UsuarioEvent extends Event{
		
		private Item item;
		private boolean confirm;
		
		public UsuarioEvent(Component source, Item item, boolean confirm) {
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
	public interface UsuarioListerner extends Serializable{
		public void onClose(UsuarioEvent event);
	}
	
}
