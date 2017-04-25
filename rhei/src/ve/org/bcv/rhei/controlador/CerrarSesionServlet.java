/*    */ package ve.org.bcv.rhei.controlador;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.RequestDispatcher;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.http.HttpServlet;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import javax.servlet.http.HttpSession;
/*    */ 
/*    */ public class CerrarSesionServlet
/*    */   extends HttpServlet
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 19 */   String aviso = "FinSesion";
/*    */   
/*    */   public void doGet(HttpServletRequest req, HttpServletResponse resp)
/*    */     throws ServletException, IOException
/*    */   {
/* 34 */     doPost(req, resp);
/*    */   }
/*    */   
/*    */   public void doPost(HttpServletRequest req, HttpServletResponse resp)
/*    */     throws ServletException, IOException
/*    */   {
/* 47 */     RequestDispatcher rd = null;
/* 48 */     String redireccion = "/jsp/login.jsp";
/* 49 */     HttpSession sesion = req.getSession(false);
/* 50 */     if (sesion != null)
/*    */     {
/* 51 */       sesion.invalidate();
/* 52 */       rd = req.getRequestDispatcher(redireccion);
/* 53 */       rd.forward(req, resp);
/*    */     }
/*    */     else
/*    */     {
/* 55 */       sesion = req.getSession(true);
/* 56 */       sesion.setAttribute("aviso", this.aviso);
/* 57 */       rd = req.getRequestDispatcher(redireccion);
/* 58 */       rd.forward(req, resp);
/*    */     }
/*    */   }
/*    */ }


/* Location:           D:\simon\proyecto_beneficios_escolares\SERVIDOR_CLASS_COMPILADOS_pr401470w7\New Folder\rhei\WEB-INF\classes\
 * Qualified Name:     ve.org.bcv.rhei.controlador.CerrarSesionServlet
 * JD-Core Version:    0.7.0.1
 */