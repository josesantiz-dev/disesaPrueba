function eliminar(pnlOp, pnlMsg, valor){ 
    if(valor!='')
    		Richfaces.showModalPanel(pnlMsg);
    else
    	Richfaces.hideModalPanel(pnlOp);
}

function edita(pnlOp, pnlMsg, valor, mens, listErrores){ 
	if(listErrores.hasChildNodes())
		return ;	
	if(valor == '')
		Richfaces.hideModalPanel(pnlOp);
	else
		Richfaces.showModalPanel(pnlMsg);
	if (mens != '')
		Richfaces.hideModalPanel(pnlOp);
}

function buscar(pnlMsg, valor){
	if(valor!='')
		Richfaces.showModalPanel(pnlMsg);
}

function nuevoimp(pnlMsg, valor){
	if(valor!='')
		Richfaces.showModalPanel(pnlMsg);
}


function nuevo(pnlOp, pnlMsg, valor){
	if(valor!='')
		Richfaces.showModalPanel(pnlMsg);
	else
    	Richfaces.showModalPanel(pnlOp);
}