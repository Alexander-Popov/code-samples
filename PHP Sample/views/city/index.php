<?php

use yii\helpers\Html;
use yii\grid\GridView;
use app\Helper\View\CityViewHelper;

/* @var $this yii\web\View */
/* @var $searchModel app\Models\Search\City\CitySearch */
/* @var $dataProvider yii\data\ActiveDataProvider */

$this->title = 'Города';
?>
<div class="city-index">

    <h1><?= Html::encode($this->title) ?></h1>

    <p>
        <?= Html::a('Создать город', ['create'], ['class' => 'btn btn-success']) ?>
    </p>
    <?= GridView::widget([
        'dataProvider' => $dataProvider,
        'filterModel' => $searchModel,
        'columns' => [
            [
                'attribute'=>'name',
                'label' => CityViewHelper::getAttributeLabel('name'),
            ],
            [
                'attribute'=>'country',
                'value' => function($data){return CityViewHelper::getCountriesArray()[$data['country']];},
                'label' => CityViewHelper::getAttributeLabel('country'),
                'filter' => Html::activeDropDownList($searchModel, 'country', CityViewHelper::getCountriesArray(),['class'=>'form-control', 'prompt' => 'Все']),
            ],
            [
                'attribute'=>'extraCharge',
                'label' => CityViewHelper::getAttributeLabel('extraCharge'),
                'value' => function($data){return $data['extra_charge'];},
            ],
            [
                'class' => 'yii\grid\ActionColumn',
                'template' =>  '{view}{update}{delete}',
                'buttons' => [
                    'update' => function ($url, $model, $key) {
                        return Html::a('Изменить',['/city/update', 'id' => $model['id']], ['class' => 'label label-primary grid-labels']);
                    },
                    'view' => function ($url, $model, $key) {
                        return Html::a('Просмотреть', ['/city/view', 'id' => $model['id']], ['class' => 'label label-info grid-labels']);
                    },
                    'delete' => function ($url, $model, $key) {
                        return Html::a('Удалить', ['/city/delete', 'id' =>  $model['id']], [
                            'data' => [
                                'confirm' => 'Вы уверены, что хотите удалить город?',
                                'method' => 'post',
                            ],
                            'class' => 'label label-danger grid-labels'
                        ]) ;
                    }
                ],
            ],
        ],
    ]); ?>
</div>
