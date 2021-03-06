package com.digital.opuserp.view.modulos.configuracoes.config_ordemServico;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.digital.opuserp.domain.SubGrupoOse;
import com.digital.opuserp.interfaces.GenericEditor;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.util.Real;
import com.digital.opuserp.util.comp.RealTextField.RealTextField;
import com.digital.opuserp.view.util.Notify;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TipoGrupoEditor extends Window implements GenericEditor {

	Item item;
	Button btSalvar;
	Button btCancelar;
	FormLayout flPrincipal;
	FieldGroup fieldGroup;
	VerticalLayout vlRoot;
	RealTextField tfValor;
	
	public TipoGrupoEditor(Item item, String title, boolean modal){
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
	}
	
	public JPAContainer<SubGrupoOse> getContainerSubGrupo(){
		JPAContainer<SubGrupoOse> container = JPAContainerFactory.make(SubGrupoOse.class, ConnUtil.getEntity());
		return container;
	}
	
	public void buildLayout(){
		
		fieldGroup = new FieldGroup(item);
		
		
		vlRoot.addComponent(
				new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);
														
					ComboBox cbSubGrupo = new ComboBox("SubGrupo", getContainerSubGrupo());			
					cbSubGrupo.setWidth("100%");				
					cbSubGrupo.setNullSelectionAllowed(false);
					cbSubGrupo.setRequired(true);
					cbSubGrupo.setItemCaptionPropertyId("nome");
					cbSubGrupo.setConverter(new SingleSelectConverter(cbSubGrupo));
					cbSubGrupo.addStyleName("caption-align");

					cbSubGrupo.focus();
					
					addComponent(cbSubGrupo);					
					fieldGroup.bind(cbSubGrupo,"subgrupo_id");
				}
			});
					
		vlRoot.addComponent(
				new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);
														
					TextField txtNomeTipoSubGrupo = new TextField ("Nome");				
					txtNomeTipoSubGrupo.setWidth("100%");				
					txtNomeTipoSubGrupo.setNullRepresentation("");
					txtNomeTipoSubGrupo.setRequired(true);
					txtNomeTipoSubGrupo.addStyleName("caption-align");
					
					addComponent(txtNomeTipoSubGrupo);					
					fieldGroup.bind(txtNomeTipoSubGrupo,"nome");
				}
			});
		
		vlRoot.addComponent(
				new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);
														
					TextArea txtDescricao = new TextArea ("Descri????o");				
					txtDescricao.setWidth("350px");	
					txtDescricao.setHeight("250px");
					txtDescricao.setNullRepresentation("");
					txtDescricao.setRequired(true);
					txtDescricao.addStyleName("caption-align");
					
					addComponent(txtDescricao);					
					fieldGroup.bind(txtDescricao,"descricao");
				}
			});
		
		vlRoot.addComponent(
				new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);						
					
					tfValor = new RealTextField("Valor (R$)");
					tfValor.setStyleName("caption-align");
					tfValor.setImmediate(true);
					tfValor.setWidth("80px");
					tfValor.setRequired(true);
					tfValor.addStyleName("align-currency");
					tfValor.setId("txtValor");
					
					if(item.getItemProperty("valor").getValue() != null){
						tfValor.setValue(Real.formatDbToString(item.getItemProperty("valor").getValue().toString()));
					}
					
					addComponent(tfValor);
					JavaScript.getCurrent().execute("$('#txtValor').maskMoney({decimal:',',thousands:'.'})");
					
					
					
				}
		});
		
		vlRoot.addComponent(
				new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);
					
					ComboBox cbStatus = new ComboBox("Status");
					cbStatus.setNullSelectionAllowed(false);
					cbStatus.setRequired(true);
					cbStatus.addItem("ATIVO");
					cbStatus.addItem("INATIVO");
					cbStatus.setStyleName("caption-align");
					
					
					addComponent(cbStatus);					
					fieldGroup.bind(cbStatus,"status");		
					
				}
			});
		
		vlRoot.addComponent(
				new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);
					
					ComboBox cbVisualizarNaOs = new ComboBox("Visualizar na Os");	
					cbVisualizarNaOs.setRequired(true);				
					cbVisualizarNaOs.setNullSelectionAllowed(false);
					cbVisualizarNaOs.addItem("SIM");
					cbVisualizarNaOs.addItem("NAO");
					cbVisualizarNaOs.setStyleName("caption-align");
					
					
					addComponent(cbVisualizarNaOs);					
					fieldGroup.bind(cbVisualizarNaOs,"visualizar_na_os");		
					
				}
			});
		
		
		
		
	}

	@Override
	public Button buildBtSalvar() {
		
		btSalvar = new Button("OK", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(fieldGroup.isValid() && tfValor.isValid()){
					try {	
						item.getItemProperty("valor").setValue(Real.formatStringToDBDouble(tfValor.getValue()));
						fieldGroup.commit();
						
						fireEvent(new TipoSubGrupoEvent(getUI(), item, true));												
						close();
					} catch (Exception e) {					
						e.printStackTrace();
						Notify.Show(e.getLocalizedMessage(), Notify.TYPE_ERROR);
					}
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
					fireEvent(new TipoSubGrupoEvent(getUI(), item, false));
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
										fireEvent(new TipoSubGrupoEvent(getUI(), item, true));					
										Notify.Show("Categoria Cadastrada com Sucesso!", Notify.TYPE_SUCCESS);
										
										close();
									} catch (Exception e) {					
										e.printStackTrace();
										Notify.Show(e.getLocalizedMessage(), Notify.TYPE_ERROR);
									}
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
							}else{							
								fieldGroup.discard();				
								fireEvent(new TipoSubGrupoEvent(getUI(), item, false));
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
	
	
	public void addListerner(TipoSubGrupoListerner target){
		try {
			Method method = TipoSubGrupoListerner.class.getDeclaredMethod("onClose", TipoSubGrupoEvent.class);
			addListener(TipoSubGrupoEvent.class, target, method);
		} catch (Exception e) {
			System.out.println("M??todo n??o Encontrado!");
		}
	}
	public void removeListerner(TipoSubGrupoListerner target){
		removeListener(TipoSubGrupoEvent.class, target);
	}
	public static class TipoSubGrupoEvent extends Event{
		
		private Item item;
		private boolean confirm;
		
		public TipoSubGrupoEvent(Component source, Item item, boolean confirm) {
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
	public interface TipoSubGrupoListerner extends Serializable{
		public void onClose(TipoSubGrupoEvent event);
	}

	
}
