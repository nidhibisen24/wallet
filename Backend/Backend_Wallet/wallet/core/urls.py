from django.urls import path
from .views import register , add_payment_account,payment_accounts,update_payment_account, create_admin,delete_payment_account ,my_referral ,set_default_payment_account ,login ,user_request_history,get_chat_room_messages, add_bonus,upload_qr_code, get_chat_rooms,send_message,get_chat_messages ,create_chat_room,approved_requests,  delete_user,all_transactions, get_qr_code,user_dashboard,search_user,add_fund_request, withdraw_fund_request,user_details , approve_request, reject_request,all_users , admin_dashboard , pending_requests

urlpatterns = [
    path('register/',register,name='register'),
    path('login/',login,name='login'),
    path("delete-user/<int:user_id>/",delete_user),

    #Fund Alya Group
    path('add-fund-request/',add_fund_request),
    path('withdraw-fund-request/',withdraw_fund_request),
    path('approve-request/',approve_request),
    path('reject-request/',reject_request),
    path('approved-requests/',approved_requests),


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


    #Chat Akaza Group
    path('create-chat-room/',create_chat_room),
    path('send-message/',send_message),
    path('chat-messages/<int:room_id>/',get_chat_messages),
    path('chat-rooms/',get_chat_rooms),
    path('chat-room-messages/<int:room_id>/',get_chat_room_messages),

    #bonus Sakamoto Group
    path('add-bonus/', add_bonus,),
    

    #Add Payment account Group dorara
    path("add-payment-account/",add_payment_account),
    path("payment-accounts/<int:user_id>/",payment_accounts),
    path("update-payment-account/", update_payment_account),
    path("delete-payment-account/",delete_payment_account),
    path("set-default-payment-account/",set_default_payment_account),

    #Referral 
    path("my-referral/<int:user_id>/",my_referral),

    path( "create-admin/", create_admin)
] 

