package com.digital.opuserp.view.modulos.configuracoes.config_estoque.pis;

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
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class PisEditor  extends Window implements GenericEditor {
	
	Item item;
	Button btSalvar;
	Button btCancelar;
	FormLayout flPrincipal;
	FieldGroup fieldGroup;
	VerticalLayout vlRoot;
	TabSheet tbDados;
	
	TextField tfTotalValor;
	
	boolean valid_desconto = false;

	public PisEditor(Item item, String title, boolean modal){
		this.item = item;
		
		
		//configLayout();	
		
		setCaption(title);
		setModal(modal);
		setResizable(false);
		setClosable(false);
		center();
		
		vlRoot = new VerticalLayout();	
		vlRoot.setWidth("100%");
		//vlRoot.setMargin(true);
		//vlRoot.setStyleName("border-form");
		
		
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
		
		
		buildTabSheet();
//		buildLayout();	

	
	}
	
	
	
	private void configLayout(){
		//1366x768
		//if(OpusERP4UI.browser.getScreenWidth() >= 1024 && OpusERP4UI.browser.getScreenHeight() >= 768){
				setWidth("800px");
				setHeight("630px");		
		//}
	}
	
	
	public void buildTabSheet(){
		
		fieldGroup = new FieldGroup(item);
		
		vlRoot.addComponent(new HorizontalLayout() {
			{
				setWidth("100%");
				setStyleName("horizontal-tabsheet");
				addComponent(new TabSheet() {
					{
						setStyleName("custom-form_tabsheet");						
						setHeight("245px");
						setWidth("494px");
						
						
						addTab(new VerticalLayout() {
							{
									{
										
										addStyleName("more-space");
										addComponent(new FormLayout() {
											{
												
												setMargin(true);
												setSpacing(true);
												setStyleName("form-cutom");
												
												TextField txtDescricao = (TextField)fieldGroup.buildAndBind("Descri????o", "descricao");				
												txtDescricao.setWidth("263px");				
												txtDescricao.setStyleName("caption-align");
												txtDescricao.setNullRepresentation("");
												txtDescricao.focus();
												txtDescricao.setRequired(true);
												txtDescricao.setMaxLength(200);
												
												addComponent(txtDescricao);
												setExpandRatio(txtDescricao, 2);		
												
											}
										});
										
										
																			
										addComponent(new FormLayout() {
											{
												
												setMargin(true);
												setSpacing(true);
												setStyleName("form-cutom");
												
												TextField txtCst = (TextField)fieldGroup.buildAndBind("Cst", "cst");				
												txtCst.setWidth("130px");				
												txtCst.setStyleName("caption-align");
												txtCst.setNullRepresentation("");
												txtCst.setMaxLength(200);
												
												addComponent(txtCst);
												setExpandRatio(txtCst, 2);		
												
											}
										});
										
										addComponent(new FormLayout() {
											{
												
												setMargin(true);
												setSpacing(true);
												setStyleName("form-cutom");
												
												TextField txtTipoPis = (TextField)fieldGroup.buildAndBind("Tipo Pis", "tipo_pis");				
												txtTipoPis.setWidth("130px");				
												txtTipoPis.setStyleName("caption-align");
												txtTipoPis.setNullRepresentation("");
												txtTipoPis.setMaxLength(200);
												
												addComponent(txtTipoPis);
												setExpandRatio(txtTipoPis, 2);		
												
											}
										});
										
										addComponent(new FormLayout() {
											{
												
												setMargin(true);
												setSpacing(true);
												setStyleName("form-cutom");
												
												TextField txtBaseCalculo = (TextField)fieldGroup.buildAndBind("Base C??lculo", "base_calculo");				
												txtBaseCalculo.setWidth("130px");				
												txtBaseCalculo.setStyleName("caption-align");
												txtBaseCalculo.setNullRepresentation("");
												txtBaseCalculo.setMaxLength(200);
												
												addComponent(txtBaseCalculo);
												setExpandRatio(txtBaseCalculo, 2);		
												
											}
										});
										

										addComponent(new HorizontalLayout(){
											{
												addComponent(new FormLayout() {
													{
														
														setMargin(true);
														setSpacing(true);
														setStyleName("form-cutom");						
																		
														TextField txtAliquota = (TextField)fieldGroup.buildAndBind("Aliquota", "aliquota");				
														txtAliquota.setWidth("130px");				
														txtAliquota.setStyleName("caption-align");
														txtAliquota.setNullRepresentation("");														
														txtAliquota.setMaxLength(200);
														
														addComponent(txtAliquota);
														setExpandRatio(txtAliquota, 2);
														
													}
												});	
												
											}
										});
										
										
										addComponent(new HorizontalLayout(){
											{
												addComponent(new FormLayout() {
													{
														
														setMargin(true);
														setSpacing(true);
														setStyleName("form-cutom");						
																		
														TextField txtValorPis = (TextField)fieldGroup.buildAndBind("Valor Pis", "valor_pis");				
														txtValorPis.setWidth("130px");				
														txtValorPis.setStyleName("caption-align");
														txtValorPis.setNullRepresentation("");														
														txtValorPis.setMaxLength(200);
														
														addComponent(txtValorPis);
														setExpandRatio(txtValorPis, 2);
														
													}
												});	
												
											}
										});
																			
									}
								}														
							},"Principal");

					}				
			});
			}
		});
		
	}


	@Override
	public Button buildBtSalvar() {
		btSalvar = new Button("OK", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(fieldGroup.isValid()){
					try {
						fieldGroup.commit();				
						fireEvent(new PisEvent(getUI(), item, true));
						
					} catch (CommitException e) {
						Notify.Show("N??o foi Possivel Salvar o Cofins!", Notify.TYPE_ERROR);
						e.printStackTrace();
					}
					close();
				}else{
					
					for (Field<?> field: fieldGroup.getFields()) {
						
						if(!field.isValid()){
							field.addStyleName("invalid-txt");
						}else{
							field.removeStyleName("invalid-txt");
						}
					}
					
					Notify.Show_Invalid_Submit_Form();
				}
				
			}
		});
		
		btSalvar.setStyleName("default");
		
		ShortcutListener clTb = new ShortcutListener("Salvar", ShortcutAction.KeyCode.ENTER, null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btSalvar.click();
			}
		};
		btSalvar.addShortcutListener(clTb);
		return btSalvar;
	}

	@Override
	public Button buildBtCancelar() {
		btCancelar = new Button("Cancelar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(!fieldGroup.isModified()){
					fieldGroup.discard();				
					fireEvent(new PisEvent(getUI(), item, false));
					
					close();
				}else{
					GenericDialog gDialog = new GenericDialog("Confirme para Continuar!", "Deseja Salvar as Informa????es Alteradas?", true, true);
					gDialog.setCaptionCANCEL("Sair sem Salvar!");
					gDialog.setCaptionOK("Salvar");
					
					gDialog.addListerner(new GenericDialog.DialogListerner() {
						
						@Override
						public void onClose(DialogEvent event) {
								if(event.isConfirm()){
									if(fieldGroup.isValid()){
										try {
											fieldGroup.commit();				
											fireEvent(new PisEvent(getUI(), item, true));
											
											Notify.Show("Cofins Salvo com Sucesso!", Notify.TYPE_SUCCESS);
											
											close();
										} catch (Exception e) {
											e.printStackTrace();
											Notify.Show("N??o foi Possivel Salvar o Cofins!", Notify.TYPE_ERROR);
										}
									}else{
										Notify.Show_Invalid_Submit_Form();
									}
								}else{							
									fieldGroup.discard();				
									fireEvent(new PisEvent(getUI(), item, false));
									close();													
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
	
	
	public void addListerner(PisListerner target){
		try {
			Method method = PisListerner.class.getDeclaredMethod("onClose", PisEvent.class);
			addListener(PisEvent.class, target, method);
		} catch (Exception e) {
			System.out.println("M??todo n??o Encontrado!");
		}
	}
	public void removeListerner(PisListerner target){
		removeListener(PisEvent.class, target);
	}
	public static class PisEvent extends Event{
		
		private Item item;
		private boolean confirm;
		
		public PisEvent(Component source, Item item, boolean confirm) {
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
	public interface PisListerner extends Serializable{
		public void onClose(PisEvent event);
	}
	
}

