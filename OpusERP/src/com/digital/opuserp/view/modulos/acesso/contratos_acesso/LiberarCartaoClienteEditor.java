package com.digital.opuserp.view.modulos.acesso.contratos_acesso;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.digital.opuserp.dao.AcessoDAO;
import com.digital.opuserp.dao.ContasReceberDAO;
import com.digital.opuserp.dao.CredenciaisAcessoDAO;
import com.digital.opuserp.domain.AcessoCliente;
import com.digital.opuserp.interfaces.GenericEditor;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.view.util.Notify;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class LiberarCartaoClienteEditor extends Window implements GenericEditor {

	private static final long serialVersionUID = 1L;
	
	boolean check_range_cartao = false;
	
	Item item;
	Button btSalvar; 
	Button btCancelar;
	FormLayout flPrincipal;
	FieldGroup fieldGroup;
	VerticalLayout vlRoot;
	
	ContasReceberDAO crDAO = new ContasReceberDAO();
	
	String valorPrimeiroBoleto;
	AcessoCliente contrato;
		
	private Label lbRegistros;
	
	HorizontalLayout hlFloat;
	
	TextField tfUploadDownload;
	TextField txtInstGratis;
	ComboBox cbInstalacaoGratis;
	
	private ComboBox cbEnderecos;
	
	
	
	TextField txtVlrCodigoCartao;
	
	String codigo_antigo;
	
	
	public LiberarCartaoClienteEditor(String title, boolean modal, String codigo_antigo, AcessoCliente contrato){
	
		this.codigo_antigo = codigo_antigo;
		this.contrato = contrato;
		
		setWidth("322px");
		setHeight("164px");
		
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
	}
	
	
	
	public String getNextId() {
		CredenciaisAcessoDAO caDao = new CredenciaisAcessoDAO();
		return caDao.getNextID();
	}
	
	public void buildLayout(){
		
		fieldGroup = new FieldGroup(item);
			
		vlRoot.addComponent(new FormLayout(){					
			{
				setStyleName("form-cutom");
				setMargin(true);
				setSpacing(true);
													
				txtVlrCodigoCartao = new TextField ("Cart??o Cliente");
				txtVlrCodigoCartao.setRequired(true);										
				txtVlrCodigoCartao.setId("txtVlrCodigoCartao");
				JavaScript.getCurrent().execute("$('#txtVlrCodigoCartao').mask('000000')");
				
				if(codigo_antigo != null){
					txtVlrCodigoCartao.setValue(codigo_antigo);
				}
				
				addComponent(txtVlrCodigoCartao);			
			
			}
		});	
	}

	@Override
	public Button buildBtSalvar() {
		
		btSalvar = new Button("OK", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(!txtVlrCodigoCartao.getValue().equals(codigo_antigo)){				
					concluirCadastro();		
				}else{
					Notify.Show("Informe um c??digo diferente!", Notify.TYPE_ERROR);
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
		//btSalvar.setEnabled(false);
		return btSalvar;
	}

	@Override
	public Button buildBtCancelar() {
		btCancelar = new Button("Cancelar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				close();				
			}
		});
		
		ShortcutListener slbtCancelar = new ShortcutListener("Cancelar", ShortcutAction.KeyCode.ESCAPE,null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btCancelar.click();
			}
		};
		
		btCancelar.addShortcutListener(slbtCancelar);
		btCancelar.setEnabled(true);
		
		return btCancelar;
	}
	
	private void concluirCadastro(){
		
		boolean check_uso_cartao = true;
		if(txtVlrCodigoCartao.isValid()){
			EntityManager em = ConnUtil.getEntity();
			Query q = em.createQuery("select a from AcessoCliente a where a.codigo_cartao =:codigo", AcessoCliente.class);
			q.setParameter("codigo", txtVlrCodigoCartao.getValue());
			
			if(q.getResultList().size() > 0){
				check_uso_cartao = false;
				Notify.Show("Este c??digo j?? est?? sendo utilizando em outro contrato, informe outro",  Notify.TYPE_ERROR);
				
			}	
			
			Integer codigo = Integer.parseInt(txtVlrCodigoCartao.getValue());			
			check_range_cartao = true;			
			
		}else{
			if(codigo_antigo != null && txtVlrCodigoCartao.getValue().equals("")){
				GenericDialog gd = new GenericDialog("Confirme para continuar", "Deseja desvincular o cart??o deste contrato ?", true, true);
				gd.addListerner(new GenericDialog.DialogListerner() {
					
					@Override
					public void onClose(DialogEvent event) {
						if(event.isConfirm()){
							boolean check  = AcessoDAO.desvincular_cartao_cliente(codigo_antigo, contrato);
							
							if(check){
								Notify.Show("Cart??o desvinculado com sucesso!", Notify.TYPE_SUCCESS);
								close();
							}
						}
					}
				});
			
				getUI().addWindow(gd); 
			}
		}
													
		if( fieldGroup.isValid() && txtVlrCodigoCartao.isValid() && check_uso_cartao && check_range_cartao){
			try {		
				
				fieldGroup.commit();			
					
				fireEvent(new LiberarCartaoClienteEvent(getUI(), item, true, txtVlrCodigoCartao.getValue() ));

			} catch (Exception e) {											
				Notify.Show("ERRO: N??o foi Possivel Salvar as Altera????es!", Notify.TYPE_ERROR);		
				e.printStackTrace();
			}
		}else{
			
			    for (Field<?> field: fieldGroup.getFields()) {						
					if(!field.isValid()){
						field.addStyleName("invalid-txt");
					}else{
						field.removeStyleName("invalid-txt");
					}
			    }
			    
			   
			    if(!check_uso_cartao){
			    	txtVlrCodigoCartao.addStyleName("invalid-txt");
			    }else{
			    	txtVlrCodigoCartao.removeStyleName("invalid-txt");
			    }
			    
			    if(!check_range_cartao){
			    	txtVlrCodigoCartao.addStyleName("invalid-txt");
			    }else{
			    	txtVlrCodigoCartao.removeStyleName("invalid-txt");
			    }

			    Notify.Show_Invalid_Submit_Form();
		}		
	}
	
	
	public void addListerner(LiberarCartaoClienteListerner target){
		try {
			Method method = LiberarCartaoClienteListerner.class.getDeclaredMethod("onClose", LiberarCartaoClienteEvent.class);
			addListener(LiberarCartaoClienteEvent.class, target, method);
		} catch (Exception e) {
			System.out.println("M??todo n??o Encontrado!");
		}
	}
	public void removeListerner(LiberarCartaoClienteListerner target){
		removeListener(LiberarCartaoClienteEvent.class, target);
	}
	public static class LiberarCartaoClienteEvent extends Event{
		
		private Item item;
		private boolean confirm;		
		private String codigo_cartao;
		
		public LiberarCartaoClienteEvent(Component source, Item item, boolean confirm, String codigo_cartao) {
			super(source);
			this.item = item;
			this.confirm = confirm;			
			this.codigo_cartao = codigo_cartao;
		}

		public Item getItem() {
			return item;
		}	

		public boolean isConfirm() {
			return confirm;
		}	
		
		public String getCodigoCartao() {
			return codigo_cartao;
		}	
		
	}
	public interface LiberarCartaoClienteListerner extends Serializable{
		public void onClose(LiberarCartaoClienteEvent event);
	}
}
