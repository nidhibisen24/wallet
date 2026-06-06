from django.urls import path
from .views import register , login , add_fund_request, withdraw_fund_request, approve_request, reject_request

urlpatterns = [
    path('register/',register,name='register'),
    path('login/',login,name='login'),


    path('add-fund-request/',add_fund_request),
    path('withdraw-fund-request/',withdraw_fund_request),
    path('approve-request/',approve_request),
    path('reject-request/',reject_request),
]