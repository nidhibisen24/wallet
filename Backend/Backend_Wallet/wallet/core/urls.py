from django.urls import path
from .views import register , login ,user_request_history, upload_qr_code ,  delete_user,all_transactions, get_qr_code,user_dashboard,search_user,add_fund_request, withdraw_fund_request,user_details , approve_request, reject_request,all_users , admin_dashboard , pending_requests

urlpatterns = [
    path('register/',register,name='register'),
    path('login/',login,name='login'),
    path("delete-user/<int:user_id>/",delete_user),

    #Fund Alya Group
    path('add-fund-request/',add_fund_request),
    path('withdraw-fund-request/',withdraw_fund_request),
    path('approve-request/',approve_request),
    path('reject-request/',reject_request),


    #admin anya Group
    path('admin-dashboard/',admin_dashboard),
    path('pending-request/',pending_requests),
    path('all-users/',all_users),
    path('user-details/<int:id>/', user_details),
    #other useful stuff
    path('search-user/', search_user),



    #User Magi Group
    path('user-dashboard/<int:id>/', user_dashboard),
    path('my-request/<int:id>/',user_request_history),


    #QR Code Obito Group
    path('upload-qr-code/',upload_qr_code),
    path('qr-code/',get_qr_code),

    #History Akaza Group
    path("all-transactions/", all_transactions),
    


]

