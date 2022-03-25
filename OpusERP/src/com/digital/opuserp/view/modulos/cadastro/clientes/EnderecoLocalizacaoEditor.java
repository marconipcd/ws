package com.digital.opuserp.view.modulos.cadastro.clientes;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.vaadin.suggestfield.SuggestField;

import com.digital.opuserp.domain.Ceps;
import com.digital.opuserp.interfaces.GenericEditor;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.view.modulos.cadastro.clientes.ClienteEditor.ClientesListerner;
import com.digital.opuserp.view.util.Notify;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
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

public class EnderecoLocalizacaoEditor extends Window implements GenericEditor {
	
	Item item;
	Button btSalvar;
	Button btCancelar;
	FormLayout flPrincipal;
	FieldGroup fgEndereco;
	VerticalLayout vlRoot;

	private String width_localizacao;

	String  cepAtual;

	public EnderecoLocalizacaoEditor(Item item, String title, boolean modal){
		
		getEnderecos();
		this.item = item;
		
		Styles styles = Page.getCurrent().getStyles();					        
        styles.add(".dashboard input.v-textfield-readonly { background-color: #FFF; }");
		
		configLayout();
		
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
				hlButtons.setSpacing(true);
				hlButtons.setMargin(true);
				hlButtons.setStyleName("hl_buttons_bottom");
				hlButtons.addComponent(buildBtCancelar());
				hlButtons.addComponent(buildBtSalvar());
				
				addComponent(hlButtons);
				setComponentAlignment(hlButtons, Alignment.BOTTOM_RIGHT);
			}
		});
					
		buildLayout();
	}
	
	private List<String> enderecos;
	private void getEnderecos(){
		Query q = ConnUtil.getEntity().createQuery("select distinct e.bairro from Ceps e ", String.class);
		enderecos = q.getResultList();
	}

	private List<Object> handleSearchQuery(String query) {
		if ("".equals(query) || query == null) {
			return Collections.emptyList();
		}
		List<String> result = new ArrayList<String>();
		
		for (String country : enderecos) {
			if (country.toLowerCase().startsWith(query.toLowerCase())) {
				result.add(country);
			}
		}
		
		if(result.size() ==0){
			result.add(query.toUpperCase());
		}
		return new ArrayList<Object>(result);
	}
	
	private void configLayout() {		
			setWidth("730px");
			setHeight("175px");
	}
	
	public void buildLayout(){
		
		fgEndereco = new FieldGroup(item);
		
		vlRoot.addComponent(new HorizontalLayout() {
			{
				addComponent(new FormLayout() {
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");
						addComponent(fgEndereco.buildAndBind("Localização","localizacao"));
						((TextField) fgEndereco.getField("localizacao")).setNullRepresentation("");
						((TextField) fgEndereco.getField("localizacao")).setStyleName("caption-align");
						((TextField) fgEndereco.getField("localizacao")).setWidth("510px");
						((TextField) fgEndereco.getField("localizacao")).setImmediate(true);
						((TextField) fgEndereco.getField("localizacao")).setTextChangeEventMode(TextChangeEventMode.LAZY);
						((TextField) fgEndereco.getField("localizacao")).setRequired(true);
						((TextField) fgEndereco.getField("localizacao")).setDescription("Link de localização");
					
					}
				});				
			}
		});

		
	}
	
	public void updateCep(Ceps cep) {
		
		if(cep != null && 
		   cep.getCep() != null && 
		   cep.getEndereco() != null && 
		   cep.getBairro() != null && 
		   cep.getCidade() != null && 
		   cep.getPais() != null && 
		   cep.getUf() != null){
		
			((TextField) fgEndereco.getField("cep")).setValue(cep.getCep());
			((TextField) fgEndereco.getField("endereco")).setValue(cep.getEndereco().toUpperCase());
			((TextField) fgEndereco.getField("numero")).setValue("");
			((SuggestField) fgEndereco.getField("bairro")).setValue(cep.getBairro().toUpperCase());
			((TextField) fgEndereco.getField("cidade")).setReadOnly(false);
			((TextField) fgEndereco.getField("cidade")).setValue(cep.getCidade().toUpperCase());
			((TextField) fgEndereco.getField("cidade")).setReadOnly(true);
			((TextField) fgEndereco.getField("uf")).setReadOnly(false);
			((TextField) fgEndereco.getField("uf")).setValue(cep.getUf().toUpperCase());
			((TextField) fgEndereco.getField("uf")).setReadOnly(true);
			((TextField) fgEndereco.getField("pais")).setReadOnly(false);
			((TextField) fgEndereco.getField("pais")).setValue(cep.getPais().toUpperCase());
			((TextField) fgEndereco.getField("pais")).setReadOnly(true);
			((TextField) fgEndereco.getField("numero")).focus();
		}
		
	}

	@Override
	public Button buildBtSalvar() {
		btSalvar = new Button("OK", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				salvarInformacoes();
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
	
	private void salvarInformacoes() {
						
			if(fgEndereco.isValid() ){
				
				try{
						fgEndereco.commit();				
						fireEvent(new LocalizacaoEvent(getUI(), item, true));						
						Notify.Show("Localização salva com sucesso!", Notify.TYPE_SUCCESS);
						close();
				}catch(Exception e){
					e.printStackTrace();
					Notify.Show("Erro ao gravar endereço", Notify.TYPE_ERROR);
				}
										
			}else{

//
//							if(txtCidade.getValue() == null || txtUf.getValue() == null || txtPais.getValue() == null){
//								Styles styles = Page.getCurrent().getStyles();					        
//								styles.add(".dashboard input.v-textfield-readonly { background-color: #E97953; }");						
//							}
//							
						
				for (Field<?> field: fgEndereco.getFields()) {
					
					if(!field.isValid()){
						field.addStyleName("invalid-txt");
						
					}else{
						field.removeStyleName("invalid-txt");
						
					}
				}
				
				Notification.show("Não é Possivel Salvar, Verifique os Campos Obrigatórios/Inválidos e Tente Novamente!");
			}
	}

	@Override
	public Button buildBtCancelar() {
		btCancelar = new Button("Cancelar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(!fgEndereco.isModified()){
					fgEndereco.discard();				
					fireEvent(new LocalizacaoEvent(getUI(), item, false));
					close();
				}else{
					GenericDialog gDialog = new GenericDialog("Confirme para Continuar!", "Deseja Salvar as Informações Alteradas?", true, true);
					gDialog.setCaptionCANCEL("Sair sem Salvar!");
					gDialog.setCaptionOK("Salvar");
					
					gDialog.addListerner(new GenericDialog.DialogListerner() {
						
						@Override
						public void onClose(DialogEvent event) {
							if(event.isConfirm()){
								salvarInformacoes();
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
	
	public void addListerner(LocalizacaoListerner target){
		try {
			Method method = LocalizacaoListerner.class.getDeclaredMethod("onClose", LocalizacaoEvent.class);
			addListener(LocalizacaoEvent.class, target, method);
		} catch (Exception e) {
			System.out.println("Método não Encontrado!");
		}
	}
	public void removeListerner(ClientesListerner target){
		removeListener(LocalizacaoEvent.class, target);
	}
	public static class LocalizacaoEvent extends Event{
		
		private Item item;
		private boolean confirm;
		
		public LocalizacaoEvent(Component source, Item item, boolean confirm) {
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
	public interface LocalizacaoListerner extends Serializable{
		public void onClose(LocalizacaoEvent event);
	}
	
	
}
