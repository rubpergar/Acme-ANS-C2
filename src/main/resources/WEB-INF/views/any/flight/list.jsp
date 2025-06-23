
<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.flight.list.label.tag" path="tag"  width="20%"/>
	<acme:list-column code="manager.flight.list.label.cost" path="cost" width="20%" />
	<acme:list-column code="manager.flight.list.label.description" path="description" width="20%" />
	<acme:list-column code="manager.flight.list.label.selfTransfer" path="selfTransfer" width="20%" />
	
</acme:list>