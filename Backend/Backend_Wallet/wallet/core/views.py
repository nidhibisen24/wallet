from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.contrib.auth.hashers import check_password

from .models import User, Wallet , FundRequest
from .serializers import RegisterSerializer , FundRequestSerializer
from django.contrib.auth import authenticate


#Register or SignUp
@api_view(['POST'])
def register(request):

    serializer = RegisterSerializer(data=request.data)

    if not serializer.is_valid():
        return Response(
            serializer.errors,
            status=400)

    full_name = serializer.validated_data['full_name']
    mobile_number = serializer.validated_data['mobile_number']
    password = serializer.validated_data['password']

    if User.objects.filter(mobile_number=mobile_number).exists():

        return Response({
            "error": "Mobile number already exists"
        }, status=400)

    user = User.objects.create_user(
        mobile_number=mobile_number,
        password=password
    )

    user.full_name = full_name
    user.save()

    Wallet.objects.create(
        user=user
    )

    return Response({
        "message": "User registered successfully",
        "user_id": user.id
    })


#Login
@api_view(['POST'])
def login(request):

    mobile_number = request.data.get('mobile_number')
    password = request.data.get('password')

    if not mobile_number or not password:
        return Response(
            {"error": "Mobile number and password required"},
            status=400
        )

    user = authenticate(
        request,
        mobile_number=mobile_number,
        password=password
    )

    if user is None:
        return Response(
            {"error": "Invalid credentials"},
            status=401
        )

    wallet = Wallet.objects.get(user=user)

    return Response({
        "message": "Login successful",
        "user_id": user.id,
        "full_name": user.full_name,
        "mobile_number": user.mobile_number,
        "role": user.role,
        "wallet_balance": wallet.balance
    })

#Add Fund Request
@api_view(['POST'])
def add_fund_request(request):

    data = request.data.copy()
    data['request_type'] = 'ADD'

    serializer = FundRequestSerializer(data=data)

    if serializer.is_valid():
        serializer.save()

        return Response({
            "message": "Add fund request submitted"
        })

    return Response(
        serializer.errors,
        status=400
    )

#Withdraw Fund Request

@api_view(['POST'])
def withdraw_fund_request(request):

    data = request.data.copy()
    data['request_type'] = 'WITHDRAW'

    serializer = FundRequestSerializer(data=data)

    if serializer.is_valid():
        serializer.save()

        return Response({
            "message": "Withdraw fund request submitted"
        })

    return Response(
        serializer.errors,
        status=400
    )


#FOr Approve Request
@api_view(['POST'])
def approve_request(request):

    request_id = request.data.get('request_id')

    try:
        fund_request = FundRequest.objects.get(
            id=request_id,
            status='PENDING'
        )

    except FundRequest.DoesNotExist:
        return Response(
            {
                "error": "Request not found"
            },
            status=404
        )

    wallet = Wallet.objects.get(
        user=fund_request.user
    )

    if fund_request.request_type == 'ADD':

        wallet.balance += fund_request.amount

        request_type = 'ADD'

    else:

        wallet.balance -= fund_request.amount

        request_type = 'WITHDRAW'

    wallet.save()

    FundRequest.objects.create(
        user=fund_request.user,
        amount=fund_request.amount,
        request_type=request_type,
        status='APPROVED'
    )

    fund_request.status = 'APPROVED'
    fund_request.save()

    return Response({
        "message": "Request approved successfully"
    })

#FOr Reject Request
@api_view(['POST'])
def reject_request(request):

    request_id = request.data.get('request_id')

    try:
        fund_request = FundRequest.objects.get(
            id=request_id,
            status='PENDING'
        )

    except FundRequest.DoesNotExist:
        return Response(
            {
                "error": "Request not found"
            },
            status=404
        )

    fund_request.status = 'REJECTED'
    fund_request.save()

    return Response({
        "message": "Request rejected successfully"
    })