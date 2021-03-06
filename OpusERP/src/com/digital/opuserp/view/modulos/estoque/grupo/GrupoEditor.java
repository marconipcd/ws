package com.digital.opuserp.view.modulos.estoque.grupo;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.digital.opuserp.OpusERP4UI;
import com.digital.opuserp.dao.GrupoProdutoDAO;
import com.digital.opuserp.domain.EmpresaFornecedor;
import com.digital.opuserp.domain.EmpresaGrupoProduto;
import com.digital.opuserp.domain.GrupoProduto;
import com.digital.opuserp.interfaces.GenericEditor;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.view.modulos.cadastro.fornecedores.AddEmpEditor;
import com.digital.opuserp.view.modulos.cadastro.fornecedores.AddEmpEditor.AddEmpresaEvent;
import com.digital.opuserp.view.modulos.estoque.grupo.AddEmpGrpEditor.AddEmpresaGrupoProdutoEvent;
import com.digital.opuserp.view.util.Notify;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class GrupoEditor extends Window implements GenericEditor {

	
	Button btSalvar;
	Button btCancelar;
	FormLayout flPrincipal;
	
	VerticalLayout vlRoot;
	TextField txtNome;
	
	GrupoProduto grupo;
	
	public GrupoEditor(GrupoProduto grupo, String title, boolean modal){
		
		this.grupo = grupo;
		
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
	
	public void buildLayout(){
		
		setWidth("700px");
		setWidth("500px");
		
					
		vlRoot.addComponent(new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);
														
					txtNome = new TextField ("Nome");				
					txtNome.setWidth("100%");				
					//tfCod.setStyleName("caption-align");
					txtNome.setNullRepresentation("");
					txtNome.setRequired(true);
					txtNome.setMaxLength(200);
					txtNome.focus();
					addComponent(txtNome);
					setExpandRatio(txtNome, 2);	
					txtNome.setTextChangeEventMode(TextChangeEventMode.LAZY);
					txtNome.addBlurListener(new FieldEvents.BlurListener() {
						
						@Override
						public void blur(BlurEvent event) {
							
							if(txtNome.getValue()!=null && txtNome.getValue().equals("")){
								((TextField) (event.getComponent())).setValue(event.getComponent().toString().toUpperCase());								
							}
								}
						});
					
					txtNome.setValue(grupo.getNome_grupo());
				}
			});
		
		
		final VerticalLayout vlEmpresas = new VerticalLayout(){
			{
				setMargin(true);
				
				
				if(grupo.getStatus().equals("EM DIGITACAO")){
					GrupoProdutoDAO.cadastrarEmpGrpPro(new EmpresaGrupoProduto(null, OpusERP4UI.getEmpresa(), grupo));
				}
				
				
				Button btAddEmpresa = new Button("Adicionar Empresa", new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						//final Fornecedor fornecedor = FornecedorDAO.find(Integer.parseInt(itemFornecedor.getItemProperty("id").toString()));
						AddEmpGrpEditor winAddEmpresa = new AddEmpGrpEditor(true, true, grupo);
						winAddEmpresa.addListerner(new AddEmpGrpEditor.AddEmpresaGrupoProdutoListerner() {
							
							@Override
							public void onSelected(AddEmpresaGrupoProdutoEvent event) {
								 if(event.getEmpresa() != null){
									 EmpresaGrupoProduto empFornecedor = new EmpresaGrupoProduto(null, event.getEmpresa(), grupo);
									 
									 GrupoProdutoDAO.cadastrarEmpGrpPro(empFornecedor);
									
									  
									 atualizarListaEmpresas();
									 
								 }
							}
						});
						
						getUI().addWindow(winAddEmpresa);
					}
				});
				
				addComponent(btAddEmpresa); 
				
				addComponent(buildTbEmpresas()); 
				
			}
		};
		
		
		final TabSheet tbRoot = new TabSheet();
		tbRoot.setSizeFull();
//		tbRoot.setHeight("422px");
//		tbRoot.setWidth("902px");
		
		
		tbRoot.addTab(vlEmpresas,"Empresas");
//		tbRoot.addTab(vlContatos,"Contatos");
//		tbRoot.addTab(vlRepresentante,"Representante");
//		tbRoot.addTab(vlEmpresas,"Empresas");
		tbRoot.setStyleName("caption-align-fornecedores-bottom");
		
    	vlRoot.addComponent(tbRoot);
			

		
		
	}
	
	Table tbEmpresas;
	private Table buildTbEmpresas(){
		
		tbEmpresas = new Table();
		tbEmpresas.setSizeFull();
		tbEmpresas.addContainerProperty("Cod", Integer.class, 0);
		tbEmpresas.addContainerProperty("Empresa", String.class, "");
		
		tbEmpresas.setColumnCollapsingAllowed(true); 
		tbEmpresas.setColumnCollapsed("Cod", true);
				
		tbEmpresas.addGeneratedColumn("r", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(final Table source,final Object itemId, Object columnId) {

				Button btRemove = new Button("Remover", new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						if(tbEmpresas.getItemIds().size() > 1){						
							EntityManager em = ConnUtil.getEntity();
							em.getTransaction().begin();
							em.remove(em.find(EmpresaGrupoProduto.class, Integer.parseInt(source.getItem(itemId).getItemProperty("Cod").getValue().toString())));
							em.getTransaction().commit();							
							
							atualizarListaEmpresas();
						}else{
							Notify.Show("N??o ?? poss??vel Remover todas as Empresas!", Notify.TYPE_WARNING);
						}
					}
				});
				btRemove.setStyleName(Reindeer.BUTTON_LINK);
				
				return btRemove;
			}
		});
		
		atualizarListaEmpresas();
		
		return tbEmpresas;
	}
	
	private void atualizarListaEmpresas(){
		if(tbEmpresas != null && grupo != null){
			
			tbEmpresas.removeAllItems();
			
			EntityManager em = ConnUtil.getEntity();
			Query qEmpresas = em.createQuery("select ef from EmpresaGrupoProduto ef where ef.grupoProduto=:grupoProduto", EmpresaGrupoProduto.class);
			qEmpresas.setParameter("grupoProduto", grupo);
			
			for (EmpresaGrupoProduto ef : (List<EmpresaGrupoProduto>)qEmpresas.getResultList()) {
				tbEmpresas.addItem(new Object[]{ef.getId(),ef.getEmpresa().getNome_fantasia()}, tbEmpresas.getItemIds().size() + 1);
			}
		}
	}

	@Override
	public Button buildBtSalvar() {
		
		btSalvar = new Button("OK", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(txtNome.isValid()){
					try {				
						
					    grupo.setNome_grupo(txtNome.getValue().toUpperCase());
					    grupo.setStatus("ATIVO");
					    
						fireEvent(new GrupoEvent(getUI(), grupo, true));					
						
					} catch (Exception e) {					
						e.printStackTrace();
						Notify.Show("ERRO: "+e.getLocalizedMessage(),Notify.TYPE_ERROR);
					}
				}else{
					
					
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
			
					GenericDialog gDialog = new GenericDialog("Confirme para Continuar!", "Deseja Salvar as Informa????es Alteradas?", true, true);
					gDialog.setCaptionCANCEL("Sair sem Salvar!");
					gDialog.setCaptionOK("Salvar");
					
					gDialog.addListerner(new GenericDialog.DialogListerner() {
						
						@Override
						public void onClose(DialogEvent event) {
							if(event.isConfirm()){
								if(txtNome.isValid()){
									try {				
										
										grupo.setNome_grupo(txtNome.getValue().toUpperCase());
										grupo.setStatus("ATIVO");
										 
										fireEvent(new GrupoEvent(getUI(), grupo, true));					
										
									} catch (Exception e) {					
										e.printStackTrace();
										Notify.Show("ERRO: "+e.getLocalizedMessage(),Notify.TYPE_ERROR);
									}
								}else{
									
									
									Notify.Show_Invalid_Submit_Form();
								}
							}else{							
							
								fireEvent(new GrupoEvent(getUI(), null, false));
								close();						
							}
						}
					});					
					
					getUI().addWindow(gDialog);
					
							
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
	
	
	public void addListerner(GrupoListerner target){
		try {
			Method method = GrupoListerner.class.getDeclaredMethod("onClose", GrupoEvent.class);
			addListener(GrupoEvent.class, target, method);
		} catch (Exception e) {
			System.out.println("M??todo n??o Encontrado!");
		}
	}
	public void removeListerner(GrupoListerner target){
		removeListener(GrupoEvent.class, target);
	}
	public static class GrupoEvent extends Event{
		
		private GrupoProduto grupoProduto;
		private boolean confirm;
		
		public GrupoEvent(Component source,GrupoProduto grupoProduto, boolean confirm) {
			super(source);
			this.grupoProduto = grupoProduto;
			this.confirm = confirm;			
		}

		public GrupoProduto getGrupoProduto() {
			return grupoProduto;
		}	

		public boolean isConfirm() {
			return confirm;
		}		
	}
	public interface GrupoListerner extends Serializable{
		public void onClose(GrupoEvent event);
	}

	
}
