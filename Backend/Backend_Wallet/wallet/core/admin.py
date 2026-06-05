from django.contrib import admin

from django.contrib import admin
from .models import User, Wallet, Transaction


@admin.register(User)
class UserAdmin(admin.ModelAdmin):
    list_display = (
        'id',
        'mobile_number',
        'full_name',
        'role',
        'is_active',
        'created_at'
    )

    search_fields = (
        'mobile_number',
        'full_name'
    )

    list_filter = (
        'role',
        'is_active'
    )


@admin.register(Wallet)
class WalletAdmin(admin.ModelAdmin):
    list_display = (
        'id',
        'user',
        'balance',
        'updated_at'
    )

    search_fields = (
        'user__mobile_number',
    )


@admin.register(Transaction)
class TransactionAdmin(admin.ModelAdmin):
    list_display = (
        'id',
        'user',
        'amount',
        'transaction_type',
        'status',
        'created_at'
    )

    search_fields = (
        'user__mobile_number',
    )

    list_filter = (
        'transaction_type',
        'status'
    )
